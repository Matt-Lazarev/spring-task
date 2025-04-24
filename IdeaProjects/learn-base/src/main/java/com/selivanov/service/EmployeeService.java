package com.selivanov.service;

import com.selivanov.dto.DepartmentDto;
import com.selivanov.dto.EmployeeDto;
import com.selivanov.entity.Department;
import com.selivanov.entity.Employee;
import com.selivanov.exception.NoSuchEmployeeException;
import com.selivanov.mapper.EmployeeDepartmentMapper;
import com.selivanov.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repository;
    private final DepartmentService departmentService;
    private final EmployeeDepartmentMapper mapper;

    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Integer id) {
        Employee employee = repository.findEmployeeById(id).orElseThrow(() ->
                new NoSuchEmployeeException("Employee with id = '%d' not found".formatted(id)));
        return mapper.toEmployeeDto(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = repository.findAll();
        return mapper.toEmployeesDto(employees);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getEmployeesByDepartment(Integer departmentId) {
        List<Employee> employees = repository.findEmployeesByDepartment(departmentId);
        return mapper.toEmployeesDto(employees);
    }

    @Transactional
    public void saveEmployee(EmployeeDto employeeDto) {
        Employee employee = mapper.toEmployee(employeeDto);

        DepartmentDto departmentDto = employeeDto.department();
        if (departmentDto != null) {
            Department department = departmentService.createOrGetDepartment(departmentDto);
            employee.setDepartment(department);
        }
        repository.save(employee);
    }

    @Transactional
    public void attachEmployeeToDepartment(Integer employeeId, Integer departmentId) {
        Employee employee = repository.findEmployeeById(employeeId).orElseThrow(() ->
                new NoSuchEmployeeException("Employee with id = '%d' not found".formatted(employeeId))
        );

        Department department = departmentService.getDepartmentById(departmentId);
        employee.setDepartment(department);

        repository.save(employee);
    }

    @Transactional
    public void createEmployeeInDepartment(Integer departmentId, EmployeeDto employeeDto) {
        //Department + Employees
        Department department = departmentService.getDepartmentById(departmentId);

        // getEmployeeById() -> 1 Employee + 1 Department
        // getDepartmentById() -> 1 Department + N Employees

        Employee employee = mapper.toEmployee(employeeDto);
        employee.setDepartment(department);

        // select exists();
        // select not exists() where name = :name;
        // insert into employees (name, job, department_id) values ('Mike', 'Developer', 1);
        if (!department.getEmployees().contains(employee)) {
            department.getEmployees().add(employee);
        }

        repository.save(employee);
    }

    @Transactional
    public void detachEmployeeFromDepartment(Integer departmentId, Integer employeeId) {
        Employee employee = repository.findEmployeeById(employeeId).orElseThrow(() ->
                new NoSuchEmployeeException("Employee with id = '%d' not found".formatted(employeeId)));

        if (employee.getDepartment() != null && employee.getDepartment().getId().equals(departmentId)) {
            employee.setDepartment(null);
            repository.save(employee);
        }
    }

    @Transactional
    public void transferEmployeeToDepartment(Integer employeeId, Integer departmentId) {
        Employee employee = repository.findById(employeeId).orElseThrow(() ->
                new NoSuchEmployeeException("Employee with id = '%d' not found".formatted(employeeId)));

        Department department = departmentService.getDepartmentById(departmentId);
        employee.setDepartment(department);
        repository.save(employee);
    }

    @Transactional
    public void updateEmployeeById(Integer id, EmployeeDto employeeDto) {
        Employee updatableEmployee = repository.findById(id).orElseThrow(() ->
                new NoSuchEmployeeException("Employee with id = '%d' not found".formatted(id))
        );

        mapper.updateEmployee(updatableEmployee, employeeDto);
        repository.save(updatableEmployee);
    }

    @Transactional
    public void deleteEmployeeById(Integer id) {
        Employee deletableEmployee = repository.findById(id).orElseThrow(() ->
                new NoSuchEmployeeException("Employee with id = '%d' not found".formatted(id))
        );

        repository.delete(deletableEmployee);
    }
}