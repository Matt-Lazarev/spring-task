package com.selivanov.client;

import com.selivanov.model.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class StudentClient {
    private final RestTemplate firstRestTemplateJwt; //3 beans found

    // local -> dev -> int/stage -> prod
    public StudentDto getStudentByName(String name) {
        return firstRestTemplateJwt.getForObject(
                "/api/students/name/%s".formatted(name),
                StudentDto.class
        );
    }
}