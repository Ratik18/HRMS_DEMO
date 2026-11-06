package com.hrms.employee.dto;

import com.hrms.employee.model.TaskStatus;
import java.time.LocalDateTime;

public record OnboardingTaskResponse(

    Long id,

    Long employeeId,

    String taskName,

    String description,

    TaskStatus status,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {}

