package com.selivanov.mapper;

import com.selivanov.dto.DepartmentDto;
import com.selivanov.dto.EmployeeDto;
import com.selivanov.entity.Department;
import com.selivanov.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeDepartmentMapper {
    @Mapping(target = "department", ignore = true)
    EmployeeDto toEmployeeDto(Employee employee);

    @Mapping(target = "department", ignore = true)
    Employee toEmployee(EmployeeDto employeeDto);
    @Mapping(target = "department", ignore = true)
    List<EmployeeDto> toEmployeesDto(List<Employee> employees);
    DepartmentDto toDepartmentDto(Department department);
    Department toDepartment(DepartmentDto departmentDto);

    @Mapping(target = "department", ignore = true)
    void updateEmployee(@MappingTarget Employee updatableEmployee, EmployeeDto employeeDto);
}