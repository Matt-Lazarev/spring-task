package com.selivanov.controller;

import com.selivanov.dto.EmployeeDto;
import com.selivanov.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService service;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = service.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    private ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Integer id) {
        EmployeeDto employee = service.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/department/{id}") //    /api/departments/{id}/employees
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartment(@PathVariable("id") Integer departmentId) {
        List<EmployeeDto> employees = service.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        service.saveEmployee(employeeDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/department/{id}")  //    /api/departments/{deptId}/employees
    public ResponseEntity<?> addEmployeeToDepartment(@PathVariable("id") Integer id,
                                                     @Valid @RequestBody EmployeeDto employeeDto) {
        service.createEmployeeInDepartment(id, employeeDto);
        return ResponseEntity.ok().build();
    }

    // OpenAPI -> Swagger
    @PutMapping("/{id}/department/{departmentId}/attach")   //    /api/departments/{deptId}/employees/{empId}
    public ResponseEntity<?> attachEmployeeToDepartment(@PathVariable("id") Integer employeeId,
                                                        @PathVariable("departmentId") Integer departmentId) {
        service.attachEmployeeToDepartment(employeeId, departmentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/department/{departmentId}/transfer")
    public ResponseEntity<?> transferEmployeeToDepartment(@PathVariable("id") Integer employeeId,
                                                          @PathVariable("departmentId") Integer departmentId) {
        service.transferEmployeeToDepartment(employeeId, departmentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Integer id,
                                            @Valid @RequestBody EmployeeDto employeeDto) {
        service.updateEmployeeById(id, employeeDto);
        return ResponseEntity.ok().build();
    }

    // employee is not deleted
    @DeleteMapping("/{id}/department/{departmentId}")
    public ResponseEntity<?> detachEmployeeFromDepartment(@PathVariable("id") Integer employeeId,
                                                          @PathVariable("departmentId") Integer departmentId) {
        service.detachEmployeeFromDepartment(departmentId, employeeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Integer id) {
        service.deleteEmployeeById(id);
        return ResponseEntity.ok().build();
    }
}