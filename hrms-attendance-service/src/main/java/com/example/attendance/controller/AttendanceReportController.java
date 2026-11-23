
package com.example.attendance.controller;

import com.example.attendance.dto.AttendanceSummaryResponse;
import com.example.attendance.service.AttendanceService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/attendance/reports")
public class AttendanceReportController {

    private final AttendanceService reportService;

    public AttendanceReportController(
            AttendanceService reportService) {
        this.reportService = reportService;
    }

    // Employee Attendance Summary
    @GetMapping("/summary/{employeeId}")
    public ResponseEntity<AttendanceSummaryResponse> getAttendanceSummary(
            @PathVariable Long employeeId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        AttendanceSummaryResponse response =
                reportService.getAttendanceSummary(
                        employeeId, startDate, endDate);

        return ResponseEntity.ok(response);
    }
}
