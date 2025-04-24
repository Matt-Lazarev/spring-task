package com.selivanov.mapper;

import com.selivanov.dto.StudentDto;
import com.selivanov.entity.Student;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
//    @InheritInverseConfiguration
    @Mapping(target = "courses", ignore = true)
    Student toStudent(StudentDto studentDto);

    StudentDto toStudentDto(Student student);

    List<StudentDto> toStudentsDto(List<Student> students);

    @Mapping(target = "courses", ignore = true)
    void updateStudent(@MappingTarget Student updatableStudent, StudentDto studentDto);
}