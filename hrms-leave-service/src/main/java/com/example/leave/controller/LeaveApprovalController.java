
package com.example.leave.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave.dto.LeaveDecisionRequest;
import com.example.leave.dto.LeaveResponse;
import com.example.leave.exception.LeaveException;
import com.example.leave.service.LeaveApprovalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leaves")
public class LeaveApprovalController {

    private final LeaveApprovalService leaveApprovalService;

    public LeaveApprovalController(
            LeaveApprovalService leaveApprovalService) {
        this.leaveApprovalService = leaveApprovalService;
    }

    // Approve a leave request
    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveResponse> approveLeave(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveDecisionRequest request,
            Authentication authentication) {

        UUID approverId = getAuthenticatedEmployeeId(authentication);

        LeaveResponse response = leaveApprovalService.approveLeave(
                id,
                approverId,
                request
        );

        return ResponseEntity.ok(response);
    }

    // Reject a leave request
    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveResponse> rejectLeave(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveDecisionRequest request,
            Authentication authentication) {

        UUID approverId = getAuthenticatedEmployeeId(authentication);

        LeaveResponse response = leaveApprovalService.rejectLeave(
                id,
                approverId,
                request
        );

        return ResponseEntity.ok(response);
    }

    // Extract approver ID from authenticated user
    private UUID getAuthenticatedEmployeeId(Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            throw new LeaveException("User is not authenticated");
        }

        try {
            return UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException ex) {
            throw new LeaveException(
                    "Authenticated user ID must be a valid UUID"
            );
        }
    }
}
