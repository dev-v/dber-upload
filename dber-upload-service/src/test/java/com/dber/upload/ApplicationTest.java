package com.dber.upload;

import com.dber.upload.config.UploadWebConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class ApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(UploadWebConfig.class,args);
    }

}
