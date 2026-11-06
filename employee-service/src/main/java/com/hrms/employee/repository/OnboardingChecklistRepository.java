package com.hrms.employee.repository;

import com.hrms.employee.model.OnboardingChecklist;
import com.hrms.employee.model.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OnboardingChecklistRepository
        extends JpaRepository<OnboardingChecklist, Long> {

    // Get all onboarding tasks of an employee
    List<OnboardingChecklist> findByEmployee_Id(Long employeeId);

    // Get tasks of an employee by status
    List<OnboardingChecklist> findByEmployee_IdAndStatus(
            Long employeeId, TaskStatus status);

    // Count completed or pending tasks
    long countByEmployee_IdAndStatus(
            Long employeeId, TaskStatus status);
}
