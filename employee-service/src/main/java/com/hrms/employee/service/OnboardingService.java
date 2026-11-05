package com.hrms.employee.service;

import com.hrms.employee.dto.OnboardingTaskResponse;
import com.hrms.employee.dto.UpdateTaskStatusRequest;

import com.hrms.employee.exception.ResourceNotFoundException;

import com.hrms.employee.model.Employee;
import com.hrms.employee.model.OnboardingChecklist;
import com.hrms.employee.model.TaskStatus;

import com.hrms.employee.repository.EmployeeRepository;
import com.hrms.employee.repository.OnboardingChecklistRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OnboardingService {

    private final OnboardingChecklistRepository onboardingRepository;
    private final EmployeeRepository employeeRepository;

    public OnboardingService(
            OnboardingChecklistRepository onboardingRepository,
            EmployeeRepository employeeRepository) {

        this.onboardingRepository = onboardingRepository;
        this.employeeRepository = employeeRepository;
    }

    // 1. Get all onboarding tasks of an employee
    @Transactional(readOnly = true)
    public List<OnboardingTaskResponse> getTasksByEmployee(
            Long employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException(
                    "Employee not found with ID: " + employeeId);
        }

        return onboardingRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 2. Get onboarding tasks by employee and status
    @Transactional(readOnly = true)
    public List<OnboardingTaskResponse> getTasksByEmployeeAndStatus(
            Long employeeId, TaskStatus status) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException(
                    "Employee not found with ID: " + employeeId);
        }

        return onboardingRepository
                .findByEmployee_IdAndStatus(employeeId, status)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 3. Get one onboarding task by ID
    @Transactional(readOnly = true)
    public OnboardingTaskResponse getTaskById(Long taskId) {

        OnboardingChecklist task = onboardingRepository
                .findById(taskId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Onboarding task not found with ID: " + taskId));

        return convertToResponse(task);
    }

    // 4. Update onboarding task status
    @Transactional
    public OnboardingTaskResponse updateTaskStatus(
            Long taskId,
            UpdateTaskStatusRequest request) {

        OnboardingChecklist task = onboardingRepository
                .findById(taskId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Onboarding task not found with ID: " + taskId));

        task.setStatus(request.status());

        OnboardingChecklist updatedTask =
                onboardingRepository.save(task);

        return convertToResponse(updatedTask);
    }

    // 5. Create an onboarding task for an employee
    @Transactional
    public OnboardingTaskResponse createTask(
            Long employeeId,
            String taskName,
            String description) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Employee not found with ID: " + employeeId));

        OnboardingChecklist task = new OnboardingChecklist();

        task.setEmployee(employee);
        task.setTaskName(taskName);
        task.setDescription(description);
        task.setStatus(TaskStatus.PENDING);

        OnboardingChecklist savedTask =
                onboardingRepository.save(task);

        return convertToResponse(savedTask);
    }

    // 6. Convert Entity into Response DTO
    private OnboardingTaskResponse convertToResponse(
            OnboardingChecklist task) {

        return new OnboardingTaskResponse(
                task.getId(),
                task.getEmployee().getId(),
                task.getTaskName(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
