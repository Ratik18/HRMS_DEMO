package com.example.attendance.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CheckOutRequest {

    @NotNull(message = "Check-out time is required")
    private LocalDateTime checkOutTime;

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
}