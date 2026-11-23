
package com.example.attendance.controller;

import com.example.attendance.dto.*;
import com.example.attendance.service.AttendanceReportService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceReportService attendanceService;

    public AttendanceController(AttendanceReportService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Employee Check-in
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(
            @RequestHeader("X-Employee-Id") Long employeeId,
            @Valid @RequestBody CheckInRequest request) {

        return ResponseEntity.ok(
                attendanceService.checkIn(employeeId, request));
    }

    // Employee Check-out
    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponse> checkOut(
            @RequestHeader("X-Employee-Id") Long employeeId,
            @Valid @RequestBody CheckOutRequest request) {

        return ResponseEntity.ok(
                attendanceService.checkOut(employeeId, request));
    }

    // View own attendance
    @GetMapping("/my")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance(
            @RequestHeader("X-Employee-Id") Long employeeId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                attendanceService.getMyAttendance(
                        employeeId, startDate, endDate));
    }

    // HR/Admin attendance
    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAllAttendance(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance(
                        startDate, endDate));
    }

    // Attendance correction
    @PutMapping("/{id}/correction")
    public ResponseEntity<AttendanceResponse> correctAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceCorrectionRequest request) {

        return ResponseEntity.ok(
                attendanceService.correctAttendance(id, request));
    }
}
