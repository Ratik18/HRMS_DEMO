package com.hrms.employee.controller;

import com.hrms.employee.dto.OnboardingTaskResponse;
import com.hrms.employee.dto.UpdateTaskStatusRequest;
import com.hrms.employee.model.TaskStatus;
import com.hrms.employee.service.OnboardingService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(
            OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    // 1. Create onBoarding task
    @PostMapping("/employees/{employeeId}/tasks")
    public ResponseEntity<OnboardingTaskResponse> createTask(
            @PathVariable Long employeeId,
            @RequestParam String taskName,
            @RequestParam(required = false) String description) {

        OnboardingTaskResponse response =
                onboardingService.createTask(
                        employeeId, taskName, description);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // 2. Get all tasks of an employee
    @GetMapping("/employees/{employeeId}/tasks")
    public ResponseEntity<List<OnboardingTaskResponse>>
            getTasksByEmployee(@PathVariable Long employeeId) {

        return ResponseEntity.ok(
                onboardingService.getTasksByEmployee(employeeId));
    }

    // 3. Get tasks by employee and status
    @GetMapping("/employees/{employeeId}/tasks/status")
    public ResponseEntity<List<OnboardingTaskResponse>>
            getTasksByEmployeeAndStatus(
                    @PathVariable Long employeeId,
                    @RequestParam TaskStatus status) {

        return ResponseEntity.ok(
                onboardingService.getTasksByEmployeeAndStatus(
                        employeeId, status));
    }

    // 4. Get individual task
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<OnboardingTaskResponse> getTaskById(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                onboardingService.getTaskById(taskId));
    }

    // 5. Update task status
    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<OnboardingTaskResponse> updateTaskStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request) {

        return ResponseEntity.ok(
                onboardingService.updateTaskStatus(taskId, request));
    }
}

