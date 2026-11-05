package com.hrms.employee.service;

import com.hrms.employee.model.Employee;
import com.hrms.employee.model.Department;
import com.hrms.employee.model.Designation;
import com.hrms.employee.model.EmploymentStatus;

import com.hrms.employee.repository.EmployeeRepository;
import com.hrms.employee.repository.DepartmentRepository;
import com.hrms.employee.repository.DesignationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            DesignationRepository designationRepository) {

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
    }

    // Create Employee
    public Employee createEmployee(Employee employee) {

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Employee email already exists");
        }

        Department department = departmentRepository.findById(
                employee.getDepartment().getId())
                .orElseThrow(() ->
                    new RuntimeException("Department not found"));

        Designation designation = designationRepository.findById(
                employee.getDesignation().getId())
                .orElseThrow(() ->
                    new RuntimeException("Designation not found"));

        employee.setDepartment(department);
        employee.setDesignation(designation);

        return employeeRepository.save(employee);
    }

    // Get All Employees with Pagination
    @Transactional(readOnly = true)
    public Page<Employee> getAllEmployees(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return employeeRepository.findAll(pageable);
    }

    // Get Employee By ID
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Employee not found: " + id));
    }

    // Get Employee By Email
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeByEmail(String email) {

        return employeeRepository.findByEmail(email);
    }

    // Update Employee
    public Employee updateEmployee(Long id, Employee updatedEmployee) {

        Employee existing = getEmployeeById(id);

        Optional<Employee> employeeWithEmail =
                employeeRepository.findByEmail(updatedEmployee.getEmail());

        if (employeeWithEmail.isPresent()
                && !employeeWithEmail.get().getId().equals(id)) {
            throw new RuntimeException("Email already exists");
        }

        Department department = departmentRepository.findById(
                updatedEmployee.getDepartment().getId())
                .orElseThrow(() ->
                    new RuntimeException("Department not found"));

        Designation designation = designationRepository.findById(
                updatedEmployee.getDesignation().getId())
                .orElseThrow(() ->
                    new RuntimeException("Designation not found"));

        existing.setFirstName(updatedEmployee.getFirstName());
        existing.setLastName(updatedEmployee.getLastName());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setPhone(updatedEmployee.getPhone());
        existing.setDateOfJoining(updatedEmployee.getDateOfJoining());
        existing.setSalary(updatedEmployee.getSalary());
        existing.setStatus(updatedEmployee.getStatus());
        existing.setDepartment(department);
        existing.setDesignation(designation);

        return employeeRepository.save(existing);
    }

    // Delete Employee
    public void deleteEmployee(Long id) {

        Employee employee = getEmployeeById(id);

        employeeRepository.delete(employee);
    }

    // Get Employees By Department
    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesByDepartment(
            Long departmentId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return employeeRepository.findByDepartmentId(
                departmentId, pageable);
    }

    // Get Employees By Status
    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesByStatus(
            EmploymentStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return employeeRepository.findByStatus(status, pageable);
    }
}