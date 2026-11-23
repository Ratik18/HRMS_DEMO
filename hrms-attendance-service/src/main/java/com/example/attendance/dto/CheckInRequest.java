package com.example.attendance.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CheckInRequest {

    @NotNull(message = "Check-in time is required")
    private LocalDateTime checkInTime;

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
}