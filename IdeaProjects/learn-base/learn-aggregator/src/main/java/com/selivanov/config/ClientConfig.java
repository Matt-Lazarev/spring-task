package com.selivanov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    // local -> dev -> int/stage -> pre-prod -> prod

    // dev:           http://dev-host/api/students/name/%s          http://integration-mock/api/students/name/%s
    // int/stage      http://int-host/api/students/name/%s          http://integration/api/students/name/%s
    // pre-prod       http://pre-prod-host/api/students/name/%s     http://integration/api/students/name/%s

    @Value("${clients.student}")
    private String studentBaseUrl;

    @Bean
    public RestTemplate studentRestTemplate() {
        // return RestTemplateBuilder().setBaseUrl(studentBaseUrl).build();
        return new RestTemplate();
    }
}
