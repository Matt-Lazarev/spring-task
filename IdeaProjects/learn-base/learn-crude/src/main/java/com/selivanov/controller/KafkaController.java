package com.selivanov.controller;

import com.selivanov.dto.StudentRequest;
import com.selivanov.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaController {
    private final StudentService studentService;


    @KafkaListener(topics = "${kafka.topics.student-request}")
    //                              com.selivanov.student.dto.StudentRequest
    //                              com.selivanov.course.dto.StudentRequest
    public void consumeStudentResult(StudentRequest studentRequest) {
        studentService.sendStudent(studentRequest);
    }
}
