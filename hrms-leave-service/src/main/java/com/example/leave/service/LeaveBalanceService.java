package com.example.leave.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.leave.dto.LeaveBalanceResponse;
import com.example.leave.model.LeaveBalance;
import com.example.leave.repository.LeaveBalanceRepository;

@Service
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveBalanceService(
            LeaveBalanceRepository leaveBalanceRepository) {
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    // 1. Get all leave balances by employee ID
    public List<LeaveBalanceResponse> getBalancesByEmployeeId(
            Long employeeId) {

        List<LeaveBalance> balances =
                leaveBalanceRepository.findAllByEmployeeId(employeeId);

        return balances.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 2. Get leave balances by employee ID and year
    public List<LeaveBalanceResponse> getBalancesByEmployeeIdAndYear(
            Long employeeId,
            Integer year) {

        List<LeaveBalance> balances =
                leaveBalanceRepository.findAllByEmployeeIdAndYear(
                        employeeId, year);

        return balances.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 3. Reserve leave balance
    @Transactional
    public void reserveBalance(
            Long employeeId,
            Long leaveTypeId,
            Integer year,
            BigDecimal days) {

        validateDays(days);

        LeaveBalance balance =
                findBalance(employeeId, leaveTypeId, year);

        balance.reserve(days);

        leaveBalanceRepository.save(balance);
    }

    // 4. Confirm leave deduction
    @Transactional
    public void confirmDeduction(
            Long employeeId,
            Long leaveTypeId,
            Integer year,
            BigDecimal days) {

        validateDays(days);

        LeaveBalance balance =
                findBalance(employeeId, leaveTypeId, year);

        balance.confirmDeduction(days);

        leaveBalanceRepository.save(balance);
    }

    // 5. Release reserved leave balance
    @Transactional
    public void releaseReservation(
            Long employeeId,
            Long leaveTypeId,
            Integer year,
            BigDecimal days) {

        validateDays(days);

        LeaveBalance balance =
                findBalance(employeeId, leaveTypeId, year);

        balance.releaseReservation(days);

        leaveBalanceRepository.save(balance);
    }

    // 6. Deduct leave balance
    @Transactional
    public void deductBalance(
            Long employeeId,
            Long leaveTypeId,
            Integer year,
            BigDecimal days) {

        validateDays(days);

        LeaveBalance balance =
                findBalance(employeeId, leaveTypeId, year);

        BigDecimal remainingDays =
                balance.getBalance()
                        .subtract(balance.getConsumed())
                        .subtract(balance.getReserved());

        if (remainingDays.compareTo(days) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient leave balance");
        }

        balance.reserve(days);
        balance.confirmDeduction(days);

        leaveBalanceRepository.save(balance);
    }

    // 7. Find leave balance
    private LeaveBalance findBalance(
            Long employeeId,
            Long leaveTypeId,
            Integer year) {

        return leaveBalanceRepository
                .findByEmployeeIdAndLeaveType_IdAndYear(
                        employeeId, leaveTypeId, year)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Leave balance not found"));
    }

    // 8. Validate leave days
    private void validateDays(BigDecimal days) {

        if (days == null ||
                days.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Days must be positive");
        }
    }

    // 9. Convert Entity into Response DTO
    private LeaveBalanceResponse convertToResponse(
            LeaveBalance balance) {

        BigDecimal remainingDays =
                balance.getBalance()
                        .subtract(balance.getConsumed())
                        .subtract(balance.getReserved());

        return new LeaveBalanceResponse(
                balance.getEmployeeId(),
                balance.getLeaveType().getId(),
                balance.getLeaveType().getName(),
                balance.getBalance(),
                balance.getConsumed(),
                remainingDays,
                balance.getYear()
        );
    }
}