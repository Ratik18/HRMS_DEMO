package com.example.leave.dto;


import java.math.BigDecimal;
import java.util.UUID;

public class LeaveBalanceResponse {

    private UUID employeeId;
    private UUID leaveTypeId;
    private String leaveTypeName;
    private BigDecimal totalDays;
    private BigDecimal usedDays;
    private BigDecimal remainingDays;
    private Integer year;

    public LeaveBalanceResponse() {
    }

    public LeaveBalanceResponse(
            UUID employeeId,
            UUID leaveTypeId,
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

    public UUID getEmployeeId() {
        return employeeId;
    }

    public UUID getLeaveTypeId() {
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