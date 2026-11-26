package com.example.leave.dto;

public class LeaveTypeResponse {

    private Long id;

    private String leaveType;

    private String description;

    private Integer allowedDays;

    public LeaveTypeResponse() {
    }

    public LeaveTypeResponse(Long id, String leaveType,
                             String description, Integer allowedDays) {

        this.id = id;
        this.leaveType = leaveType;
        this.description = description;
        this.allowedDays = allowedDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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