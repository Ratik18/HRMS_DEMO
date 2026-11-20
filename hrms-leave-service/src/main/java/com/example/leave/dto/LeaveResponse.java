package com.example.leave.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.leave.model.LeaveStatus;

public record LeaveResponse(

		UUID id,

		UUID employeeId,

		UUID leaveTypeId,

        LocalDate startDate,

        LocalDate endDate,

        BigDecimal totalDays,

        String reason,

        LeaveStatus status,

        UUID approverId,

        String decisionComment,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}



