package com.example.leave.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LeaveApplicationRequest(

        @NotNull(message = "Leave type ID is required")
        Long leaveTypeId,

        @NotNull(message = "Start date is required")
        @FutureOrPresent(message = "Start date cannot be in the past")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @NotBlank(message = "Leave reason is required")
        @Size(max = 500, message = "Reason cannot exceed 500 characters")
        String reason

) {
}

