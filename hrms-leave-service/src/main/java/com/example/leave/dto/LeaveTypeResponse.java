package com.example.leave.dto;

import java.util.UUID;

public class LeaveTypeResponse {

    private UUID id;
    private String leaveType;
    private String description;
    private Integer allowedDays;

    public LeaveTypeResponse() {
    }

    public LeaveTypeResponse(UUID id, String leaveType,
                             String description, Integer allowedDays) {
        this.id = id;
        this.leaveType = leaveType;
        this.description = description;
        this.allowedDays = allowedDays;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAllowedDays() {
        return allowedDays;
    }

    public void setAllowedDays(Integer allowedDays) {
        this.allowedDays = allowedDays;
    }
}

