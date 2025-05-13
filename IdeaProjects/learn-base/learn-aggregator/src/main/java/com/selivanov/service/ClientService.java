package com.selivanov.service;

import com.selivanov.client.StudentClient;
import com.selivanov.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//                           logic
// RestTemplate <- Client <- Service

@Service
@RequiredArgsConstructor
public class ClientService {
    private final StudentClient studentClient;

    public StudentDto getStudentByName(String name) {
        return studentClient.getStudentByName(name);
    }

    public void consumeStudentResponse(StudentDto studentDto) {
        System.out.println("name: " + studentDto.name() + ", courses: " + studentDto.courses());
    }
}
