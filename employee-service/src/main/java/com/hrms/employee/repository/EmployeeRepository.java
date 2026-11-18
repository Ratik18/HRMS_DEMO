

package com.hrms.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hrms.employee.model.Employee;
import com.hrms.employee.model.EmploymentStatus;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    Page<Employee> findByDepartmentId(
            Long departmentId, Pageable pageable);

    Page<Employee> findByStatus(
            EmploymentStatus status, Pageable pageable);
}
