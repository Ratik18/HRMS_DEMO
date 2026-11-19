package com.example.leave.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.leave.model.LeaveRequest;
import com.example.leave.model.LeaveStatus;

import jakarta.persistence.LockModeType;

@Repository
public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {

    // Find all leave requests submitted by an employee
    List<LeaveRequest> findByEmployeeIdOrderByCreatedAtDesc(
            Long employeeId);

    // Find leave requests by status
    List<LeaveRequest> findByStatusOrderByCreatedAtAsc(
            LeaveStatus status);

    // Find a specific leave request belonging to an employee
    Optional<LeaveRequest> findByIdAndEmployeeId(
            Long id,
            Long employeeId);

    // Check whether an employee has an overlapping leave request
    @Query("""
        SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END
        FROM LeaveRequest l
        WHERE l.employeeId = :employeeId
          AND l.status IN :statuses
          AND l.startDate <= :endDate
          AND l.endDate >= :startDate
    """)
    boolean existsOverlappingRequest(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("statuses") List<LeaveStatus> statuses
    );

    // Lock a leave request while processing approval or rejection
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT l
        FROM LeaveRequest l
        WHERE l.id = :id
    """)
    Optional<LeaveRequest> findByIdForUpdate(
            @Param("id") Long id);
}

