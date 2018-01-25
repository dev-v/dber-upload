package com.dber.upload.server.qiniu;

import com.dber.upload.server.config.UploadConfig;
import com.dber.upload.server.valid.AbstractUploader;
import com.dber.upload.server.valid.IIDGenerator;
import com.qiniu.util.Auth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

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

    @Autowired
    private IIDGenerator iidGenerator;

    private Auth auth;

    @PostConstruct
    private void init() {
        UploadConfig.Auth auth = uploadConfig.getAuth();
        this.auth = Auth.create(auth.getAccessKey(), auth.getSecretKey());
    }

    @Override
    public String getRealUploadToken(String bucket) {
        return auth.uploadToken(bucket, String.valueOf(iidGenerator.next()),
                uploadConfig.getUploadExpireSeconds(), PUT_POLICY_RETURN_BODY, true);
    }

    @Override
    public String getDownloadToken(String baseUrl) {
        return auth.privateDownloadUrl(baseUrl, uploadConfig.getDownloadExpireSeconds());
    }

    @Override
    protected boolean realCallback(String authorization, String url, String contentType, byte[] content) {
        return auth.isValidCallback(authorization, url, content, contentType);
    }
}