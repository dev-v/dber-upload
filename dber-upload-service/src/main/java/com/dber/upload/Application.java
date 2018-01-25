package com.dber.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.dber.upload.config.UploadWebConfig;

/**
 * <li>文件名称: Application.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
@Configuration
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(UploadWebConfig.class, args);
	}
}
