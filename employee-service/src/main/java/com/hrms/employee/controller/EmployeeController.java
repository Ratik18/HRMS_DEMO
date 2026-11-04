package com.hrms.employee.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hrms.employee.model.Employee;
import com.hrms.employee.model.EmploymentStatus;
import com.hrms.employee.service.EmployeeService;
import com.hrms.employee.model.Department;
import com.hrms.employee.model.Designation;



import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create Employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee) {

        Employee saved =
                employeeService.createEmployee(employee);

        return new ResponseEntity<>(
                saved, HttpStatus.CREATED);
    }

    // Get All Employees with Pagination
    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Employee> employees =
                employeeService.getAllEmployees(page, size);

        return ResponseEntity.ok(employees);
    }

    // Get Employee By ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id) {

        Employee employee =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(employee);
    }

    // Get Employee By Email
    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(
            @PathVariable String email) {

        Optional<Employee> employee =
                employeeService.getEmployeeByEmail(email);

        return employee.map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        Employee updated =
                employeeService.updateEmployee(id, employee);

        return ResponseEntity.ok(updated);
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // Get Employees By Department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<Employee>> getEmployeesByDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Employee> employees =
                employeeService.getEmployeesByDepartment(
                        departmentId, page, size);

        return ResponseEntity.ok(employees);
    }

    // Get Employees By Status
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Employee>> getEmployeesByStatus(
            @PathVariable EmploymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Employee> employees =
                employeeService.getEmployeesByStatus(
                        status, page, size);

        return ResponseEntity.ok(employees);
    }
}