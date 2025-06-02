package com.selivanov.config;

import com.selivanov.config.jwt.JwtRestTemplateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/*
    University-service    :   http://localhost:8080
    - /api/students
    - /api/teachers
    - /api/courses

    UniversityServiceClientConfig {
            // local -> dev -> int/stage -> prod
        @Value("${clients.university}")
        private String baseUrl = "http://localhost:8080";

        @Bean
        public RestTemplate universityRestTemplate(RestTemplateBuilder builder) {
            DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(baseUrl);
            return builder
                    .uriTemplateHandler(uriBuilderFactory)
                    .build();
        }
    }
 */

@Configuration
@RequiredArgsConstructor
public class FirstClientConfig {
    private final JwtRestTemplateInterceptor jwtInterceptor;

    @Value("${clients.student}")
    private String studentBaseUrl = "http://localhost:8080";

    @Bean
    public RestTemplate firstRestTemplateJwt(RestTemplateBuilder builder) {
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(studentBaseUrl);
        return builder
                .interceptors(jwtInterceptor)
                .uriTemplateHandler(uriBuilderFactory)
                .build();
    }

    @Bean
    public RestTemplate firstRestTemplatePlain(RestTemplateBuilder builder) {
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(studentBaseUrl);
        return builder
                .uriTemplateHandler(uriBuilderFactory)
                .build();
    }
}