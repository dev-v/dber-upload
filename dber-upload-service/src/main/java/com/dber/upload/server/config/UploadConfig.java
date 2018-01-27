package com.dber.upload.server.config;

import com.dber.upload.server.qiniu.strategy.ImgStyle;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/24
 */
@Data
@ConfigurationProperties("dber.upload")
public class UploadConfig {
    private int uploadExpireSeconds;
    private int downloadExpireSeconds;

    private int tokenMaxSize;

    private Auth auth = new Auth();

    private ImgStyle imgStyle;

    private String callbackUrl;


    private Buckets buckets = new Buckets();

    @Data
    public static final class Buckets {
        private Bucket pub = new Bucket();
        private Bucket pri = new Bucket();
    }

    @Data
    public static final class Bucket {
        private String name;
        /**
         * 下载地址
         */
        private String url;

        /**
         * 上传地址
         */
        private String upUrl;
    }

    @Data
    public static class Auth {
        private String accessKey;
        private String secretKey;
    }

}
