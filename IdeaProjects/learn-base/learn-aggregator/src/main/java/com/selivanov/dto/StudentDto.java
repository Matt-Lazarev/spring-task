package com.selivanov.dto;

import java.util.List;
public record StudentDto(
        Integer id,
        String name,
        List<CourseDto> courses
) {
}
