package com.selivanov.service;

import com.selivanov.dto.StudentDto;
import com.selivanov.entity.Course;
import com.selivanov.entity.Student;
import com.selivanov.exception.NoSuchStudentException;
import com.selivanov.mapper.StudentMapper;
import com.selivanov.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repository;
    private final StudentMapper mapper;
    private final CourseService courseService;

    @Transactional(readOnly = true)
    public StudentDto getStudentById(Integer id) {
        Student student = repository.findById(id).orElseThrow(
                () -> new NoSuchStudentException("Student with id = '%d' not found".formatted(id))
        );

        return mapper.toStudentDto(student);
    }

    @Transactional(readOnly = true)
    public StudentDto getStudentByName(String name) {
        Student student = repository.getStudentByName(name).orElseThrow(
                () -> new NoSuchStudentException("Student with id = '%s' not found".formatted(name))
        );
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mapper.toStudentDto(student);
    }

    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudents() {
        List<Student> students = repository.findAll();
        return mapper.toStudentsDto(students);
    }

    @Transactional
    public void saveStudent(StudentDto studentDto) {
        Student student = mapper.toStudent(studentDto);
        repository.save(student);
    }

    @Transactional
    public void attachCourseToStudent(String courseName, Integer studentId) {
        if (courseName.isBlank()) {
            throw new RuntimeException();
        }

        Student student = repository.findById(studentId).orElseThrow(
                () -> new NoSuchStudentException("Student with id = '%d' not found".formatted(studentId))
        );

        Course course = courseService.getCourseByName(courseName);

        // exists student by id
        // select course by name
        // insert into students_courses values (s_id, c_id);
        student.getCourses().add(course);
        repository.save(student);
    } //?

    @Transactional
    public void updateStudentById(Integer id, StudentDto studentDto) {
        Student updatableStudent = repository.findById(id).orElseThrow(
                () -> new NoSuchStudentException("Student with id = '%d' not found".formatted(id))
        );
        mapper.updateStudent(updatableStudent, studentDto);
        repository.save(updatableStudent);
    } // Обновление должно выполнять с изменением course или без?

    @Transactional
    public void detachCourseFromStudent(String courseName, Integer studentId) {
//        if (courseName != null && !courseName.isBlank() && studentId != null) {
//            Student student = repository.findById(studentId).orElseThrow(
//                    () -> new NoSuchStudentException("Student with id = '%d' not found".formatted(studentId))
//            );
//            Course course = courseService.getCourseByName(courseName);
//
//            student.getCourses().remove(course);
//            repository.save(student);
//        }

        // exists student by id
        // select course by name [c_id]
        // delete from students_courses where s_id = :s_id, c_id = :c_id;
    }

    @Transactional
    public void deleteStudent(Integer id) {
        Student student = repository.findById(id).orElseThrow(
                () -> new NoSuchStudentException("Student with id = '%d' not found".formatted(id))
        );
        repository.delete(student);
    }
}