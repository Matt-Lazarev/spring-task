package com.selivanov.client;

import com.selivanov.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class StudentClient {
    private final RestTemplate studentRestTemplate;

    public StudentDto getStudentByName(String name) {
        return studentRestTemplate.getForObject(
                "/api/students/name/%s".formatted(name),
                StudentDto.class
        );
    }
}
