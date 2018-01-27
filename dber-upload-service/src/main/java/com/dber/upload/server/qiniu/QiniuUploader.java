package com.dber.upload.server.qiniu;

import com.dber.upload.api.entity.UploadToken;
import com.dber.upload.server.config.UploadConfig;
import com.dber.upload.server.valid.AbstractUploader;
import com.qiniu.util.Auth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

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
@Configuration
@Service
public class QiniuUploader extends AbstractUploader {

    private static final Log log = LogFactory.getLog(QiniuUploader.class);

    @Autowired
    private UploadConfig uploadConfig;

    private Auth auth;

    @PostConstruct
    private void init() {
        UploadConfig.Auth auth = uploadConfig.getAuth();
        this.auth = Auth.create(auth.getAccessKey(), auth.getSecretKey());
    }

    @Override
    public UploadToken getRealUploadToken(String bucket, long key) {
        UploadToken token = new UploadToken();
        String _key = String.valueOf(key);
        token.setKey(_key);
        token.setToken(auth.uploadToken(bucket, _key, uploadConfig.getUploadExpireSeconds(), PUT_POLICY_CALL_BACK, false));
        return token;
    }

    @Override
    public String getDownloadUrl(String baseUrl) {
        return auth.privateDownloadUrl(baseUrl, uploadConfig.getDownloadExpireSeconds());
    }

    @Override
    protected boolean realCallback(String authorization, String url, String contentType, String content) {
        return auth.isValidCallback(authorization, url, content.getBytes(), contentType);
    }
}