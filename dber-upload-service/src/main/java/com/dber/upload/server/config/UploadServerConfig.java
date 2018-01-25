package com.dber.upload.server.config;

import com.dber.upload.server.qiniu.QiniuUploader;
import com.dber.upload.server.valid.AttackValidService;
import com.dber.upload.server.valid.IDGeneratorService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/25
 */
@Configuration
@EnableConfigurationProperties(UploadConfig.class)
@Import({IDGeneratorService.class, AttackValidService.class, QiniuUploader.class})
public class UploadServerConfig {

}
