package com.example.attendance.dto;

public class AttendanceSummaryResponse {

    private Long employeeId;
    private int totalPresent;
    private int totalAbsent;
    private int totalHalfDay;
    private int totalLeave;
    private int totalHolidays;
    private int totalWeekOff;

    public AttendanceSummaryResponse() {
    }

    public AttendanceSummaryResponse(
            Long employeeId,
            int totalPresent,
            int totalAbsent,
            int totalHalfDay,
            int totalLeave,
            int totalHolidays,
            int totalWeekOff) {

        this.employeeId = employeeId;
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.totalHalfDay = totalHalfDay;
        this.totalLeave = totalLeave;
        this.totalHolidays = totalHolidays;
        this.totalWeekOff = totalWeekOff;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public int getTotalPresent() {
        return totalPresent;
    }

    public int getTotalAbsent() {
        return totalAbsent;
    }

    public int getTotalHalfDay() {
        return totalHalfDay;
    }

    public int getTotalLeave() {
        return totalLeave;
    }

    public int getTotalHolidays() {
        return totalHolidays;
    }

    public int getTotalWeekOff() {
        return totalWeekOff;
    }
}