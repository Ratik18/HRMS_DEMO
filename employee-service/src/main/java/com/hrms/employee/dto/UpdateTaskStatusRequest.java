package com.hrms.employee.dto;

import com.hrms.employee.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(

    @NotNull(message = "Task status is required")
    TaskStatus status

) {}

