package com.hrms.employee.service;

import com.hrms.employee.model.Department;
import com.hrms.employee.repository.DepartmentRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Create Department
    public Department createDepartment(Department department) {

        if (departmentRepository.existsByNameIgnoreCase(
                department.getName())) {
            throw new RuntimeException("Department already exists");
        }

        return departmentRepository.save(department);
    }

    // Get All Departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get Department By ID
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Department not found with ID: " + id));
    }

    // Update Department
    public Department updateDepartment(Long id, Department updatedDepartment) {

        Department existing = getDepartmentById(id);

        if (!existing.getName().equalsIgnoreCase(updatedDepartment.getName())
                && departmentRepository.existsByNameIgnoreCase(
                        updatedDepartment.getName())) {
            throw new RuntimeException("Department name already exists");
        }

        existing.setName(updatedDepartment.getName());
        existing.setDescription(updatedDepartment.getDescription());

        return departmentRepository.save(existing);
    }

    // Delete Department
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
    }
}