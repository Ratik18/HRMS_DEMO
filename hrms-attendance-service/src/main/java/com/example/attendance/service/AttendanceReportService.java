 package com.example.attendance.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.dto.AttendanceCorrectionRequest;
import com.example.attendance.dto.AttendanceResponse;
import com.example.attendance.dto.CheckInRequest;
import com.example.attendance.dto.CheckOutRequest;
import com.example.attendance.exception.AttendanceException;
import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.AttendanceStatus;
import com.example.attendance.repository.AttendanceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceReportService {

    private final AttendanceRepository repository;

    public AttendanceReportService(AttendanceRepository repository) {
        this.repository = repository;
    }

    // Employee Check-in
    @Transactional
    public AttendanceResponse checkIn(
            Long employeeId, CheckInRequest request) {

        LocalDateTime checkInTime = request.getCheckInTime();

        if (checkInTime == null) {
            throw new AttendanceException("Check-in time is required");
        }

        if (checkInTime.isAfter(LocalDateTime.now())) {
            throw new AttendanceException("Future check-in is not allowed");
        }

        LocalDate date = checkInTime.toLocalDate();

        if (repository.existsByEmployeeIdAndAttendanceDate(
                employeeId, date)) {
            throw new AttendanceException(
                    "Attendance already exists for this date");
        }

        AttendanceRecord record = new AttendanceRecord();

        record.setEmployeeId(employeeId);
        record.setAttendanceDate(date);
        record.setCheckIn(checkInTime);
        record.setStatus(AttendanceStatus.PRESENT);

        AttendanceRecord saved = repository.save(record);

        return convertToResponse(saved);
    }

    // Employee Check-out
    @Transactional
    public AttendanceResponse checkOut(
            Long employeeId, CheckOutRequest request) {

        LocalDateTime checkOutTime = request.getCheckOutTime();

        if (checkOutTime == null) {
            throw new AttendanceException("Check-out time is required");
        }

        if (checkOutTime.isAfter(LocalDateTime.now())) {
            throw new AttendanceException("Future check-out is not allowed");
        }

        LocalDate date = checkOutTime.toLocalDate();

        AttendanceRecord record = repository
                .findByEmployeeIdAndAttendanceDate(employeeId, date)
                .orElseThrow(() -> new AttendanceException(
                        "Check-in record not found"));

        if (record.getCheckOut() != null) {
            throw new AttendanceException("Already checked out");
        }

        if (checkOutTime.isBefore(record.getCheckIn())) {
            throw new AttendanceException(
                    "Check-out cannot be earlier than check-in");
        }

        record.setCheckOut(checkOutTime);

        return convertToResponse(repository.save(record));
    }

    // Get employee attendance
    public List<AttendanceResponse> getMyAttendance(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        return repository
                .findByEmployeeIdAndAttendanceDateBetween(
                        employeeId, startDate, endDate)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get all employee attendance
    public List<AttendanceResponse> getAllAttendance(
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        return repository
                .findByAttendanceDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Attendance correction
    @Transactional
    public AttendanceResponse correctAttendance(
            Long id,
            AttendanceCorrectionRequest request) {

        AttendanceRecord record = repository.findById(id)
                .orElseThrow(() -> new AttendanceException(
                        "Attendance record not found"));

        if (request.getStatus() == null) {
            throw new AttendanceException("Status is required");
        }

        LocalDateTime checkIn = request.getCheckIn();
        LocalDateTime checkOut = request.getCheckOut();

        if (checkIn != null && checkOut != null
                && checkOut.isBefore(checkIn)) {
            throw new AttendanceException(
                    "Check-out cannot be earlier than check-in");
        }

        if (checkIn != null
                && !checkIn.toLocalDate().equals(record.getAttendanceDate())) {
            throw new AttendanceException(
                    "Check-in date must match attendance date");
        }

        if (checkOut != null
                && !checkOut.toLocalDate().equals(record.getAttendanceDate())) {
            throw new AttendanceException(
                    "Check-out date must match attendance date");
        }

        record.setCheckIn(checkIn);
        record.setCheckOut(checkOut);
        record.setStatus(request.getStatus());
        record.setRemarks(request.getRemarks());

        return convertToResponse(repository.save(record));
    }

    // Validate date range
    private void validateDateRange(
            LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new AttendanceException(
                    "Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new AttendanceException(
                    "Start date cannot be after end date");
        }
    }

    // Convert Entity to DTO
    private AttendanceResponse convertToResponse(
            AttendanceRecord record) {

        return new AttendanceResponse(
                record.getId(),
                record.getEmployeeId(),
                record.getAttendanceDate(),
                record.getCheckIn(),
                record.getCheckOut(),
                record.getStatus(),
                record.getRemarks()
        );
    }
}