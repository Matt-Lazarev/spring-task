package com.selivanov.controller;

import com.selivanov.dto.StudentDto;
import com.selivanov.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Integer id) {
        StudentDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<StudentDto> getStudentByName(@PathVariable("name") String name) {
        StudentDto student = studentService.getStudentByName(name);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<?> saveStudent(@Valid @RequestBody StudentDto studentDto) {
        studentService.saveStudent(studentDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{studentId}/courses/{courseName}")
    public ResponseEntity<?> attachCourseToStudent(
            @PathVariable("studentId") Integer studentId,
            @PathVariable("courseName") String courseName) {
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
            @PathVariable("courseName") String courseName) {
        studentService.detachCourseFromStudent(courseName, studentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}