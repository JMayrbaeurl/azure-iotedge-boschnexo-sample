package com.microsoft.samples.nexo.uploader.nexofileuploader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * SpringTestConfig
 */
@Configuration
public class SpringTestConfig {

    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FileUploader getUploader() {
        FileUploader uploader = new FileUploader();

        return uploader;
    }
}