package com.dber.upload.server.valid;

import com.alibaba.fastjson.JSON;
import com.dber.base.enums.ImgErrorStatus;
import com.dber.base.enums.ImgErrorType;
import com.dber.base.enums.ImgStatus;
import com.dber.base.enums.ImgType;
import com.dber.upload.api.entity.Dfile;
import com.dber.upload.api.entity.DfileError;
import com.dber.upload.server.UploadResult;
import com.dber.upload.server.Uploader;
import com.dber.upload.server.config.UploadConfig;
import com.dber.upload.service.IDfileErrorService;
import com.dber.upload.service.IDfileService;
import com.dber.util.ExpireMap;
import com.qiniu.util.StringMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

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
    ExpireMap<String, Img> expireMap;

    private static final Log log = LogFactory.getLog(AbstractUploader.class);

    @Autowired
    private IDfileService fileService;

    @Autowired
    private IDfileErrorService fileErrorService;

    @Autowired
    private IAttackValid attackValid;

    @Autowired
    private UploadConfig uploadConfig;

    @Override
    public String getUploadToken(ImgType imgType, long bsId) {
        attackValid.isAttack(imgType.getValue(), bsId);
        String bucket = imgType.isPublic() ? uploadConfig.getBucket().getImge() : uploadConfig.getBucket().getImgePrivate();
        String token = getRealUploadToken(bucket);
        expireMap.put(token, new Img(imgType.getValue(), bsId));
        return token;
    }

    @Override
    public String getDownloadUrl() {
        return uploadConfig.getBucket().getImge();
    }

    @Override
    public String getPrivateDownloadUrl() {
        return uploadConfig.getBucket().getImgePrivate();
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
    public void callback(String authorization, String contentType, byte[] content) {
        if (realCallback(authorization, uploadConfig.getCallbackUrl(), contentType, content)) {
            UploadResult result = JSON.parseObject(content, UploadResult.class);
            String token = result.getToken();
            if (token == null) {//不回传token
                fileErrorService.save(result.toErrorFile(ImgErrorType.NOTOKEN));
                return;
            }
            Img img = expireMap.get(result.getToken());
            if (img == null) {//超时或者超容量后丢失 记录异常信息
                fileErrorService.save(result.toErrorFile(ImgErrorType.EXPIRE));
                return;
            }
            if (img.getBsId() == result.getBsId() && img.getType() == result.getType()) {//业务验证成功
                fileService.save(result.toFile());
            } else {//业务验证失败 发现滥用或攻击行为 记录业务ID并拒绝上传token申请
                DfileError error = result.toErrorFile(ImgErrorType.ATTACK);
                error.setErrorType(img.getType());
                error.setBsId(img.getBsId());
                fileErrorService.save(error);
                attackValid.add(img.getType(), img.getBsId());
                return;
            }
        }
    }

    /**
     * 子类验证回源回调 若有效返回true
     *
     * @param authorization
     * @param url
     * @param contentType
     * @param content
     * @return
     */
    protected abstract boolean realCallback(String authorization, String url, String contentType, byte[] content);

    @Override
    public long[] getKeys(int type, long bsId) {
        Dfile dfile = new Dfile();
        dfile.setBsId(bsId);
        dfile.setType(type);
        dfile.setStatus(ImgStatus.AVAILABLE.getValue());
        return fileService.getIds(dfile);
    }

    public abstract String getRealUploadToken(String bucket);

    protected static final StringMap PUT_POLICY_RETURN_BODY = new StringMap();

    protected static final StringMap PUT_POLICY_CALL_BACK = new StringMap();

    @PostConstruct
    private void init() {
        StringMap RESPONSE_BODY = new StringMap();
        RESPONSE_BODY.put("key", "$(key)");
        RESPONSE_BODY.put("key", "$(token)");
        RESPONSE_BODY.put("key", "$(endUser)");
        RESPONSE_BODY.put("hash", "$(etag)");
        RESPONSE_BODY.put("bucket", "$(bucket)");
        RESPONSE_BODY.put("fname", "$(fname)");
        RESPONSE_BODY.put("fsize", "$(fsize)");
        RESPONSE_BODY.put("type", "$(x:type)");
        RESPONSE_BODY.put("bsId", "$(x:bsId)");

        PUT_POLICY_RETURN_BODY.put("returnBody", RESPONSE_BODY);

        PUT_POLICY_CALL_BACK.put("callbackBody", RESPONSE_BODY);
        PUT_POLICY_CALL_BACK.put("callbackUrl", uploadConfig.getCallbackUrl());
        PUT_POLICY_CALL_BACK.put("callbackBodyType", "application/json");

        this.expireMap = new ExpireMap<>(uploadConfig.getTokenMaxSize(), uploadConfig.getUploadExpireSeconds() + 600);
    }
}
