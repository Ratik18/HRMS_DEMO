package com.example.attendance.dto;


import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.example.attendance.model.AttendanceStatus;

public class AttendanceCorrectionRequest {

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    @NotNull(message = "Attendance status is required")
    private AttendanceStatus status;

    private String remarks;

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}