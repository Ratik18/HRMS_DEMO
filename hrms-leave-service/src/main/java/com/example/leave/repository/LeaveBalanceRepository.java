package com.example.leave.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.leave.model.LeaveBalance;

@Repository
public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, Long> {

    List<LeaveBalance> findAllByEmployeeId(Long employeeId);

    List<LeaveBalance> findAllByEmployeeIdAndYear(
            Long employeeId, Integer year);

    Optional<LeaveBalance> findByEmployeeIdAndLeaveType_IdAndYear(
            Long employeeId,
            Long leaveTypeId,
            Integer year);
}