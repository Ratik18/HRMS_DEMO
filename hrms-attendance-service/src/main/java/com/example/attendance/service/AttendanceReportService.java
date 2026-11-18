package com.example.attendance.service;


import org.springframework.stereotype.Service;

import com.example.attendance.dto.AttendanceSummaryResponse;
import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.HolidayRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceReportService {

    private final AttendanceRepository attendanceRepository;
    private final HolidayRepository holidayRepository;

    public AttendanceReportService(
            AttendanceRepository attendanceRepository,
            HolidayRepository holidayRepository) {

        this.attendanceRepository = attendanceRepository;
        this.holidayRepository = holidayRepository;
    }

    public AttendanceSummaryResponse getMonthlySummary(
            Long employeeId,
            int year,
            int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);

        LocalDate endDate = startDate.withDayOfMonth(
            startDate.lengthOfMonth()
        );

        List<AttendanceRecord> records =
            attendanceRepository
                .findByEmployeeIdAndAttendanceDateBetween(
                    employeeId,
                    startDate,
                    endDate
                );

        int present = 0;
        int absent = 0;
        int halfDay = 0;
        int leave = 0;
        int holiday = 0;
        int weekOff = 0;

        for (AttendanceRecord record : records) {

            switch (record.getStatus()) {

                case PRESENT -> present++;

                case ABSENT -> absent++;

                case HALF_DAY -> halfDay++;

                case ON_LEAVE -> leave++;

                case HOLIDAY -> holiday++;

                case WEEK_OFF -> weekOff++;
            }
        }

        int workingDays = 0;

        for (LocalDate date = startDate;
             !date.isAfter(endDate);
             date = date.plusDays(1)) {

            boolean isHoliday =
                holidayRepository.existsByHolidayDate(date);

            boolean isSunday =
                date.getDayOfWeek() == DayOfWeek.SUNDAY;

            if (!isHoliday && !isSunday) {
                workingDays++;
            }
        }

        return new AttendanceSummaryResponse(
            employeeId,
            workingDays,
            present,
            absent,
            halfDay,
            leave,
            holiday,
            weekOff
        );
    }
}