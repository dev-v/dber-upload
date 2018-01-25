package com.dber.upload.config;

import com.dber.plat.api.PlatLoginHelper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.dber.base.config.BaseWebConfig;

/**
 * <li>文件名称: WebConfig.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
@Configuration
@Import({ UploadServiceConfig.class,BaseWebConfig.class, PlatLoginHelper.class})
@ComponentScan("com.dber.upload.web")
public class UploadWebConfig {
}
