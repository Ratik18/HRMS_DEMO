package com.hrms.employee.repository;

import com.hrms.employee.model.Employee;
import com.hrms.employee.model.EmploymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
    Optional<Employee> findByEmail(String email);
    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);
    Page<Employee> findByStatus(EmploymentStatus status, Pageable pageable);
}
