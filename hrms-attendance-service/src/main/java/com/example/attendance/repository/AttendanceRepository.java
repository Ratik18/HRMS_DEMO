package com.example.attendance.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.AttendanceStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository
        extends JpaRepository<AttendanceRecord, Long> {

    // Find attendance by employee and date
    Optional<AttendanceRecord> findByEmployeeIdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );

    // Get employee attendance between dates
    List<AttendanceRecord> findByEmployeeIdAndAttendanceDateBetween(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );

    // Get all attendance between dates
    List<AttendanceRecord> findByAttendanceDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    // Count attendance by employee and status
    long countByEmployeeIdAndStatusAndAttendanceDateBetween(
            Long employeeId,
            AttendanceStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    // Check duplicate attendance
    boolean existsByEmployeeIdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );
}