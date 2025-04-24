package com.selivanov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeDto(
        Integer id,
        @Size(min = 1, max = 21, message = "Name should be from 1 to 21 symbols")
        String name,
        @Size(min = 3, max = 21, message = "Job should be from 3 to 21 symbols")
        String job,
        DepartmentDto department
) {
}