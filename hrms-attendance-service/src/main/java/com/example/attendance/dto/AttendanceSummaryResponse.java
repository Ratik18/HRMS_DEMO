package com.example.attendance.dto;



public record AttendanceSummaryResponse(

    Long employeeId,

    int totalWorkingDays,

    int presentDays,

    int absentDays,

    int halfDays,

    int leaveDays,

    int holidays,

    int weekOffs

) {}