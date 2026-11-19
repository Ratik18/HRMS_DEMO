package com.example.leave.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.leave.model.LeaveStatus;

public record LeaveResponse(

        Long id,

        Long employeeId,

        Long leaveTypeId,

        LocalDate startDate,

        LocalDate endDate,

        BigDecimal totalDays,

        String reason,

        LeaveStatus status,

        Long approverId,

        String decisionComment,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}

