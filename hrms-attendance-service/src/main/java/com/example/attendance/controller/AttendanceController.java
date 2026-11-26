
package com.example.attendance.controller;

import com.example.attendance.dto.*;
import com.example.attendance.service.AttendanceReportService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceReportService attendanceService;

    public AttendanceController(
            AttendanceReportService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Employee Check-in

@PostMapping("/check-in")
public ResponseEntity<AttendanceResponse> checkIn(
        Authentication authentication,
        @Valid @RequestBody CheckInRequest request) {

    Long employeeId = getEmployeeId(authentication);

    AttendanceResponse result =
            attendanceService.checkIn(employeeId, request);

    return ResponseEntity
            .status(201)
            .body(result);
}

    // Employee Check-out
    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponse> checkOut(
            Authentication authentication,
            @Valid @RequestBody CheckOutRequest request) {

        Long employeeId = getEmployeeId(authentication);

        return ResponseEntity.ok(
                attendanceService.checkOut(employeeId, request));
    }

    // Own Attendance
    @GetMapping("/my")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance(
            Authentication authentication,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        Long employeeId = getEmployeeId(authentication);

        return ResponseEntity.ok(
                attendanceService.getMyAttendance(
                        employeeId, startDate, endDate));
    }

    // HR/Admin Attendance
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

    // HR/Admin Correction
    @PutMapping("/{id}/correction")
    public ResponseEntity<AttendanceResponse> correctAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceCorrectionRequest request) {

        return ResponseEntity.ok(
                attendanceService.correctAttendance(id, request));
    }

    private Long getEmployeeId(Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {
            throw new org.springframework.security.authentication
                    .AuthenticationCredentialsNotFoundException(
                            "User not authenticated");
        }

        return Long.valueOf(authentication.getName());
    }
}
