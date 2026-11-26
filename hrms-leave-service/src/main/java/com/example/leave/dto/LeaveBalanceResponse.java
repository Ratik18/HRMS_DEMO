package com.example.leave.dto;

import java.math.BigDecimal;

public class LeaveBalanceResponse {

    private Long employeeId;
    private Long leaveTypeId;
    private String leaveTypeName;
    private BigDecimal totalDays;
    private BigDecimal usedDays;
    private BigDecimal remainingDays;
    private Integer year;

    public LeaveBalanceResponse() {
    }

    public LeaveBalanceResponse(
            Long employeeId,
            Long leaveTypeId,
            String leaveTypeName,
            BigDecimal totalDays,
            BigDecimal usedDays,
            BigDecimal remainingDays,
            Integer year) {

        this.employeeId = employeeId;
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeName = leaveTypeName;
        this.totalDays = totalDays;
        this.usedDays = usedDays;
        this.remainingDays = remainingDays;
        this.year = year;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getLeaveTypeId() {
        return leaveTypeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public BigDecimal getTotalDays() {
        return totalDays;
    }

    public BigDecimal getUsedDays() {
        return usedDays;
    }

    public BigDecimal getRemainingDays() {
        return remainingDays;
    }

    public Integer getYear() {
        return year;
    }
}