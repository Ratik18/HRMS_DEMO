
package com.example.leave.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.leave.dto.LeaveApplicationRequest;
import com.example.leave.dto.LeaveResponse;
import com.example.leave.exception.LeaveException;
import com.example.leave.model.LeaveRequest;
import com.example.leave.model.LeaveStatus;
import com.example.leave.repository.LeaveRequestRepository;

@Service
@Transactional
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    // Apply for a new leave
    public LeaveResponse applyForLeave(
            UUID employeeId,
            LeaveApplicationRequest request) {

        validateEmployeeId(employeeId);

        if (request == null) {
            throw new LeaveException("Leave application details are required");
        }

        validateLeaveDates(request.startDate(), request.endDate());

        // Check whether the employee already has an overlapping request.
        List<LeaveStatus> overlappingStatuses = List.of(
                LeaveStatus.PENDING,
                LeaveStatus.APPROVED
        );

        boolean overlapping =
                leaveRequestRepository.existsOverlappingRequest(
                        employeeId,
                        request.startDate(),
                        request.endDate(),
                        overlappingStatuses
                );

        if (overlapping) {
            throw new LeaveException(
                    "You already have a pending or approved leave "
                    + "request for the selected dates"
            );
        }

        BigDecimal totalDays = calculateLeaveDays(
                request.startDate(),
                request.endDate()
        );

        LeaveRequest leaveRequest = new LeaveRequest();

        leaveRequest.setEmployeeId(employeeId);
        leaveRequest.setLeaveTypeId(request.leaveTypeId());
        leaveRequest.setStartDate(request.startDate());
        leaveRequest.setEndDate(request.endDate());
        leaveRequest.setTotalDays(totalDays);
        leaveRequest.setReason(request.reason());
        leaveRequest.setStatus(LeaveStatus.PENDING);

        LeaveRequest savedRequest =
                leaveRequestRepository.save(leaveRequest);

        return convertToResponse(savedRequest);
    }

    // Get all leave requests belonging to an employee
    @Transactional(readOnly = true)
    public List<LeaveResponse> getMyLeaves(UUID employeeId) {

        validateEmployeeId(employeeId);

        return leaveRequestRepository
                .findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get a specific leave request belonging to an employee
    @Transactional(readOnly = true)
    public LeaveResponse getMyLeaveById(
            UUID leaveId,
            UUID employeeId) {

        validateEmployeeId(employeeId);

        if (leaveId == null) {
            throw new LeaveException("Invalid leave request ID");
        }

        LeaveRequest leaveRequest = leaveRequestRepository
                .findByIdAndEmployeeId(leaveId, employeeId)
                .orElseThrow(() -> new LeaveException(
                        "Leave request not found"
                ));

        return convertToResponse(leaveRequest);
    }

    // Get leave requests by status
    @Transactional(readOnly = true)
    public List<LeaveResponse> getLeavesByStatus(LeaveStatus status) {

        if (status == null) {
            throw new LeaveException("Leave status is required");
        }

        return leaveRequestRepository
                .findByStatusOrderByCreatedAtAsc(status)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Cancel a pending leave request
    public LeaveResponse cancelLeave(
            UUID leaveId,
            UUID employeeId) {

        validateEmployeeId(employeeId);

        if (leaveId == null) {
            throw new LeaveException("Invalid leave request ID");
        }

        LeaveRequest leaveRequest = leaveRequestRepository
                .findByIdAndEmployeeId(leaveId, employeeId)
                .orElseThrow(() -> new LeaveException(
                        "Leave request not found"
                ));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new LeaveException(
                    "Only pending leave requests can be cancelled"
            );
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);

        LeaveRequest updatedRequest =
                leaveRequestRepository.save(leaveRequest);

        return convertToResponse(updatedRequest);
    }

    // Validate employee ID
    private void validateEmployeeId(UUID employeeId) {

        if (employeeId == null) {
            throw new LeaveException("Invalid employee ID");
        }
    }

    // Validate leave dates
    private void validateLeaveDates(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new LeaveException(
                    "Start date and end date are required"
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new LeaveException(
                    "End date cannot be before start date"
            );
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new LeaveException(
                    "Leave cannot be applied for past dates"
            );
        }
    }

    // Calculate inclusive calendar days
    private BigDecimal calculateLeaveDays(
            LocalDate startDate,
            LocalDate endDate) {

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        return BigDecimal.valueOf(days).setScale(2);
    }

    // Convert entity into response DTO
    private LeaveResponse convertToResponse(
            LeaveRequest leaveRequest) {

        return new LeaveResponse(
        		leaveRequest.getId(),
                leaveRequest.getEmployeeId(),
                leaveRequest.getLeaveTypeId(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getTotalDays(),
                leaveRequest.getReason(),
                leaveRequest.getStatus(),
                leaveRequest.getApproverId(),
                leaveRequest.getDecisionComment(),
                leaveRequest.getCreatedAt(),
                leaveRequest.getUpdatedAt()
        );
    }
}
