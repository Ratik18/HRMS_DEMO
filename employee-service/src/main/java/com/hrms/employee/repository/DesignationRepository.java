package com.hrms.employee.repository;

import com.hrms.employee.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
    boolean existsByTitleIgnoreCase(String title);
}
