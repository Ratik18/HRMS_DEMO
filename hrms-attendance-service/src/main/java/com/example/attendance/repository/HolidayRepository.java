package com.example.attendance.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.model.Holiday;

import java.time.LocalDate;
import java.util.Optional;

public interface HolidayRepository
        extends JpaRepository<Holiday, Long> {

    Optional<Holiday> findByHolidayDate(LocalDate holidayDate);

    boolean existsByHolidayDate(LocalDate holidayDate);
}