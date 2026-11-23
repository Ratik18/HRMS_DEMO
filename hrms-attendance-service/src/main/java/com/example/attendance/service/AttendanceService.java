package com.example.attendance.service;



import org.springframework.stereotype.Service;

import com.example.attendance.dto.AttendanceSummaryResponse;
import com.example.attendance.model.AttendanceStatus;
import com.example.attendance.repository.AttendanceRepository;

import java.time.LocalDate;

@Service
public class AttendanceService {

    private final AttendanceRepository repository;

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    public AttendanceSummaryResponse getAttendanceSummary(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null || endDate == null
                || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Invalid attendance date range");
        }

        int present = (int) repository
                .countByEmployeeIdAndStatusAndAttendanceDateBetween(
                        employeeId, AttendanceStatus.PRESENT,
                        startDate, endDate);

        int absent = (int) repository
                .countByEmployeeIdAndStatusAndAttendanceDateBetween(
                        employeeId, AttendanceStatus.ABSENT,
                        startDate, endDate);

        int halfDay = (int) repository
                .countByEmployeeIdAndStatusAndAttendanceDateBetween(
                        employeeId, AttendanceStatus.HALF_DAY,
                        startDate, endDate);

        int leave = (int) repository
                .countByEmployeeIdAndStatusAndAttendanceDateBetween(
                        employeeId, AttendanceStatus.ON_LEAVE,
                        startDate, endDate);

        int holidays = (int) repository
                .countByEmployeeIdAndStatusAndAttendanceDateBetween(
                        employeeId, AttendanceStatus.HOLIDAY,
                        startDate, endDate);

        int weekOff = (int) repository
                .countByEmployeeIdAndStatusAndAttendanceDateBetween(
                        employeeId, AttendanceStatus.WEEK_OFF,
                        startDate, endDate);

        return new AttendanceSummaryResponse(
                employeeId,
                present,
                absent,
                halfDay,
                leave,
                holidays,
                weekOff
        );
    }
}