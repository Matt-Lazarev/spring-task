package com.selivanov.service;

import com.selivanov.client.StudentClient;
import com.selivanov.dto.StudentDto;
import com.selivanov.dto.StudentRequest;
import com.selivanov.entity.Course;
import com.selivanov.entity.Student;
import com.selivanov.exception.NoSuchEntityException;
import com.selivanov.mapper.StudentMapper;
import com.selivanov.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repository;
    private final StudentMapper mapper;
    private final CourseService courseService;
    private final StudentClient studentClient;

    private TransactionTemplate transactionTemplate;

    @Transactional(readOnly = true)
    public StudentDto getStudentById(Integer id) {
        Student student = repository.findStudentById(id).orElseThrow(
                () -> new NoSuchEntityException("Student with id = '%d' not found".formatted(id))
        );

        return mapper.toStudentDto(student);
    }

    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudents() {
        List<Student> students = repository.findStudents();
        return mapper.toStudentsDto(students);
    }

    @Transactional
    public void saveStudent(StudentDto studentDto) {
        Student student = mapper.toStudent(studentDto);
        repository.save(student);
    }

    @Transactional
    public void attachCourseToStudent(String courseName, Integer studentId) {
        Student student = repository.findById(studentId).orElseThrow(
                () -> new NoSuchEntityException("Student with id = '%d' not found".formatted(studentId))
        );
        Course course = courseService.getCourseByName(courseName);
        repository.saveStudentsCourses(student.getId(), course.getId());
    }

    @Transactional
    public void updateStudentById(Integer id, StudentDto studentDto) {
        Student updatableStudent = repository.findById(id).orElseThrow(
                () -> new NoSuchEntityException("Student with id = '%d' not found".formatted(id))
        );
        mapper.updateStudent(updatableStudent, studentDto);
        repository.save(updatableStudent);
    }

    @Transactional
    public void detachCourseFromStudent(String courseName, Integer studentId) {
        Student student = repository.findById(studentId).orElseThrow(
                () -> new NoSuchEntityException("Student with id = '%d' not found".formatted(studentId))
        );
        Course course = courseService.getCourseByName(courseName);
        repository.deleteFromStudentsCourses(student.getId(), course.getId());
    }

    @Transactional
    public void deleteStudent(Integer id) {
        Student student = repository.findById(id).orElseThrow(
                () -> new NoSuchEntityException("Student with id = '%d' not found".formatted(id))
        );
        repository.delete(student);
    }

    public void sendStudent(StudentRequest studentRequest) {
        StudentDto student = getStudentByName(studentRequest.name());
        studentClient.sendStudent(student);
    }

    // connection pool (20 connections)
    // transaction -> getConnection from pool
    // 20 transaction getStudentByName -> 0 connection in pool
    public StudentDto getStudentByName(String name) {
        Student student = transactionTemplate.execute((status) ->
            repository.findStudentByName(name).orElseThrow(
                    () -> new NoSuchEntityException("Student with id = '%s' not found".formatted(name))
            )
        );

        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mapper.toStudentDto(student);
    }
}