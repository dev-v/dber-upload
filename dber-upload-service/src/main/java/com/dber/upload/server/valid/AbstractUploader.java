package com.dber.upload.server.valid;

import com.alibaba.fastjson.JSON;
import com.dber.base.enums.ImgErrorStatus;
import com.dber.base.enums.ImgErrorType;
import com.dber.base.enums.ImgStatus;
import com.dber.base.enums.ImgType;
import com.dber.upload.api.entity.Dfile;
import com.dber.upload.api.entity.DfileError;
import com.dber.upload.api.entity.DownloadUrlRequest;
import com.dber.upload.api.entity.UploadToken;
import com.dber.upload.server.UploadResult;
import com.dber.upload.server.Uploader;
import com.dber.upload.server.config.UploadConfig;
import com.dber.upload.service.IDfileErrorService;
import com.dber.upload.service.IDfileService;
import com.dber.util.ExpireMap;
import com.dber.util.Util;
import com.qiniu.util.StringMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/24
 */
public abstract class AbstractUploader implements Uploader {

    private static final Log log = LogFactory.getLog(AbstractUploader.class);

    @Autowired
    private IDfileService fileService;

    @Autowired
    private IDfileErrorService fileErrorService;

    @Autowired
    private IAttackValid attackValid;

    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private IIDGenerator iidGenerator;

    ExpireMap<String, Img> expireMap;

    UploadConfig.Bucket pubBucket;

    UploadConfig.Bucket priBucket;

    @Override
    public UploadToken getUploadToken(ImgType imgType, long bsId) {
        attackValid.isAttack(imgType.getValue(), bsId);
        Dfile dfile = new Dfile();
        dfile.setType(imgType.getValue());
        dfile.setBsId(bsId);
        dfile.setStatus(ImgStatus.AVAILABLE.getValue());
        if (fileService.count(dfile) < imgType.getMaxCount()) {
            String bucket = imgType.isPublic() ? pubBucket.getName() : priBucket.getName();
            UploadToken token = getRealUploadToken(bucket, iidGenerator.next());
            expireMap.put(token.getToken(), new Img(imgType.getValue(), bsId));
            return token;
        } else {
            throw new IllegalStateException("该项最多只能上传【" + imgType.getMaxCount() + "】张图片，您可以用新图片替换之前上传的图片！");
        }
    }

    @Override
    public UploadToken coverUploadToken(ImgType imgType, long bsId, long id) {
        attackValid.isAttack(imgType.getValue(), bsId);
        Dfile dfile = validSame(imgType, bsId, id);
        if (dfile != null) {
            dfile.setStatus(ImgStatus.COVER.getValue());
            fileService.save(dfile);
            String bucket = imgType.isPublic() ? pubBucket.getName() : priBucket.getName();
            UploadToken token = getRealUploadToken(bucket, iidGenerator.next());
            expireMap.put(token.getToken(), new Img(imgType.getValue(), bsId));
            return token;
        }
        return null;
    }

    @Override
    public int del(ImgType imgType, long bsId, long id) {
        Dfile dfile = validSame(imgType, bsId, id);
        int count = 0;
        if (dfile != null) {
            dfile.setStatus(ImgStatus.DELETED.getValue());
            count = fileService.save(dfile);
        }
        return count;
    }

    @Override
    public String getUploadUrl(ImgType imgType) {
        return imgType.isPublic() ? pubBucket.getUpUrl() : priBucket.getUpUrl();
    }

    @Override
    public String[] getDownloadUrls(DownloadUrlRequest request) {
        ImgType type = ImgType.from(request.getType());
        long[] keys = getKeys(type, request.getBsId());
        String[] urls = new String[keys.length];
        for (int i = 0, len = keys.length; i < len; i++) {
            urls[i] = getUrl(type, keys[i], request.getProtocol(), request.getStyle());
        }
        return urls;
    }

    private String getUrl(ImgType type, long key, String protocol, String style) {
        style = Util.isBlank(style) ? "" : '-' + style;
        String url = protocol + getDownloadUrl(type) + '/' + key + style;
        return type.isPublic() ? url : getDownloadUrl(url);

    }

    @Override
    public String getDownloadUrl(ImgType imgType) {
        return imgType.isPublic() ? pubBucket.getUrl() : priBucket.getUrl();
    }

    @Override
    public int removeAttack(ImgType imgType, long bsId) {
        attackValid.remove(imgType.getValue(), bsId);
        DfileError error = new DfileError();
        error.setType(imgType.getValue());
        error.setBsId(bsId);
        error.setStatus(ImgErrorStatus.INVALID.getValue());
        return fileErrorService.save(error);
    }

    @Override
    public String callback(String authorization, String contentType, String content) {
        if (realCallback(authorization, uploadConfig.getCallbackUrl(), contentType, content)) {
            UploadResult result = JSON.parseObject(content, UploadResult.class);
            String token = result.getToken();
            result.setToken(null);
            if (Util.isBlank(token)) {//不回传token
//            fileErrorService.save(result.toErrorFile(ImgErrorType.NOTOKEN));
                return null;
            }

            Img img = expireMap.get(token);
            if (img == null) {//超时或者超容量后丢失 记录异常信息
                fileErrorService.save(result.toErrorFile(ImgErrorType.EXPIRE));
                return null;
            }
            if (img.getBsId() == result.getBsId() && img.getType() == result.getType()) {//业务验证成功
                fileService.save(result.toFile());

                Map<String, String> returnBody = new HashMap<>();
                returnBody.put("url", getUrl(ImgType.from(result.getType()), result.getKey(), result.getProtocol(), result.getStyle()));
                
                return JSON.toJSONString(returnBody);
            } else {//业务验证失败 发现滥用或攻击行为 记录业务ID并拒绝上传token申请
                DfileError error = result.toErrorFile(ImgErrorType.ATTACK);
                error.setErrorType(img.getType());
                error.setBsId(img.getBsId());
                fileErrorService.save(error);
                attackValid.add(img.getType(), img.getBsId());
                return null;
            }
        }
        return null;
    }

    /**
     * 子类验证回源回调 若有效返回true
     *
     * @param authorization
     * @param url
     * @param contentType
     * @return
     */
    protected abstract boolean realCallback(String authorization, String url, String contentType, String content);

    @Override
    public long[] getKeys(ImgType imgType, long bsId) {
        Dfile dfile = new Dfile();
        dfile.setBsId(bsId);
        dfile.setType(imgType.getValue());
        dfile.setStatus(ImgStatus.AVAILABLE.getValue());
        return fileService.getIds(dfile);
    }

    public abstract UploadToken getRealUploadToken(String bucket, long key);

    protected static final StringMap PUT_POLICY_CALL_BACK = new StringMap();

    /**
     * 验证是否有对应一致的数据
     * 有返回对应数据
     * 否则返回null
     *
     * @return
     */
    private Dfile validSame(ImgType imgType, long bsId, long id) {
        Dfile dfile = new Dfile();
        dfile.setId(id);
        dfile.setType(imgType.getValue());
        dfile.setBsId(bsId);
        dfile = fileService.queryOne(dfile);
        return dfile;
    }

    @PostConstruct
    private void init() {
        Map<String, String> callbackBody = new HashMap<>();
        callbackBody.put("key", "$(key)");
        callbackBody.put("endUser", "$(endUser)");
        callbackBody.put("hash", "$(etag)");
        callbackBody.put("bucket", "$(bucket)");
        callbackBody.put("fname", "$(fname)");
        callbackBody.put("fsize", "$(fsize)");
        callbackBody.put("token", "$(x:token)");
        callbackBody.put("type", "$(x:type)");
        callbackBody.put("bsId", "$(x:bsId)");
        callbackBody.put("protocol", "$(x:protocol)");
        callbackBody.put("style", "$(x:style)");
        PUT_POLICY_CALL_BACK.put("callbackBody", JSON.toJSONString(callbackBody));

        Map<String, String> returnBody = new HashMap<>();
        returnBody.put("url", "$(x:url)");
        PUT_POLICY_CALL_BACK.put("url", JSON.toJSONString(returnBody));

        PUT_POLICY_CALL_BACK.put("callbackUrl", uploadConfig.getCallbackUrl());
        PUT_POLICY_CALL_BACK.put("callbackBodyType", "application/json");

        pubBucket = uploadConfig.getBuckets().getPub();
        priBucket = uploadConfig.getBuckets().getPri();
        this.expireMap = new ExpireMap<>(uploadConfig.getTokenMaxSize(), uploadConfig.getUploadExpireSeconds() + 600);
    }
}
