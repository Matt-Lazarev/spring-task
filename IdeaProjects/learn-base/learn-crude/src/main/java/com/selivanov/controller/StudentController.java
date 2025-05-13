package com.selivanov.controller;

import com.selivanov.dto.StudentDto;
import com.selivanov.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @Value("${kafka.topics.student-request}")
    private String studentTopic;

    private final KafkaTemplate<Integer, StudentDto> kafkaTemplate;

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Integer id) {
        StudentDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<StudentDto> getStudentByName(@PathVariable("name") String name) {
        StudentDto student = studentService.getStudentByName(name);
        kafkaTemplate.send(studentTopic, student);
        log.info("Message sent to Kafka in topic" + studentTopic);
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<?> saveStudent(@Valid @RequestBody StudentDto studentDto) {
        studentService.saveStudent(studentDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{studentId}/courses/{courseName}")
    public ResponseEntity<?> attachCourseToStudent(
            @PathVariable("studentId") Integer studentId,
            @PathVariable("courseName") @NotBlank @Size(min = 1, max = 50) String courseName) {
        studentService.attachCourseToStudent(courseName, studentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentById(@PathVariable("id") Integer id,
                                               @Valid @RequestBody StudentDto studentDto) {
        studentService.updateStudentById(id, studentDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studentId}/courses/{courseName}")
    public ResponseEntity<?> detachCourseFromStudent(
            @PathVariable("studentId") Integer studentId,
            @PathVariable("courseName") @NotBlank @Size(min = 1, max = 50) String courseName) {
        studentService.detachCourseFromStudent(courseName, studentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}