package com.selivanov.service;

import com.selivanov.client.StudentClient;
import com.selivanov.model.StudentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//                           logic
//RestTemplate <- Client <- Service
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final StudentClient studentClient;

    @Value("${kafka.topics.student-request}")
    private String studentTopic;

    public StudentDto getStudentByName(String name) {
        return studentClient.getStudentByName(name);
    }

    public void consumeStudentResponse(StudentDto studentDto) {
        log.info("Message sent to KafkaProducer in topic" + studentTopic);
    }
}