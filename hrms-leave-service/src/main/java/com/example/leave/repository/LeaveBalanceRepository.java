package com.example.leave.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.leave.model.LeaveBalance;

@Repository
public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, UUID> {

    List<LeaveBalance> findAllByEmployeeId(UUID employeeId);

    List<LeaveBalance> findAllByEmployeeIdAndYear(
            UUID employeeId, Integer year);

    Optional<LeaveBalance> findByEmployeeIdAndLeaveType_IdAndYear(
            UUID employeeId,
            UUID leaveTypeId,
            Integer year);
}
