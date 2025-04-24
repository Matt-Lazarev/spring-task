package com.selivanov.dto;

import java.util.List;

public record DepartmentEmployeesDto(
        List<EmployeeDto> employees,
        DepartmentDto department
) {
}
