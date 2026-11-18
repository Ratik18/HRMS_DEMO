package com.example.attendance.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "attendance_holidays",
    schema = "attendance"
)
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "holiday_date",
        nullable = false,
        unique = true
    )
    private LocalDate holidayDate;

    @Column(
        name = "holiday_name",
        nullable = false,
        length = 150
    )
    private String holidayName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }
}