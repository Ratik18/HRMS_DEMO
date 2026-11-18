package com.example.attendance.controller;


import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.attendance.dto.AttendanceSummaryResponse;
import com.example.attendance.service.AttendanceReportService;

@RestController
@RequestMapping("/api/attendance/reports")
public class AttendanceReportController {

    private final AttendanceReportService reportService;

    public AttendanceReportController(
            AttendanceReportService reportService) {

        this.reportService = reportService;
    }

    @GetMapping("/my/monthly")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public AttendanceSummaryResponse getMyMonthlyReport(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam int year,
            @RequestParam int month) {

        Long employeeId = jwt.getClaim("employee_id");

        return reportService.getMonthlySummary(
            employeeId,
            year,
            month
        );
    }
}