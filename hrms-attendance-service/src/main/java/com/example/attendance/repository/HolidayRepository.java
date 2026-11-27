package com.example.attendance.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.Holiday;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository
        extends JpaRepository<Holiday, Long> {

    // Find holiday by date
    Optional<Holiday> findByHolidayDate(LocalDate holidayDate);

    // Check holiday exists
    boolean existsByHolidayDate(LocalDate holidayDate);

    // Get holidays between dates
    List<Holiday> findByHolidayDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}