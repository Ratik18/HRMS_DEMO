
package com.example.leave.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave.dto.LeaveApplicationRequest;
import com.example.leave.dto.LeaveResponse;
import com.example.leave.exception.LeaveException;
import com.example.leave.model.LeaveStatus;
import com.example.leave.service.LeaveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // Apply for leave
    @PostMapping
    public ResponseEntity<LeaveResponse> applyForLeave(
            @Valid @RequestBody LeaveApplicationRequest request,
            Authentication authentication) {

        UUID employeeId = getAuthenticatedEmployeeId(authentication);

        LeaveResponse response =
                leaveService.applyForLeave(employeeId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get logged-in employee's leave history
    @GetMapping("/my")
    public ResponseEntity<List<LeaveResponse>> getMyLeaves(
            Authentication authentication) {

        UUID employeeId = getAuthenticatedEmployeeId(authentication);

        return ResponseEntity.ok(
                leaveService.getMyLeaves(employeeId)
        );
    }

    // Get one leave request belonging to the logged-in employee
    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponse> getMyLeaveById(
            @PathVariable UUID id,
            Authentication authentication) {

        UUID employeeId = getAuthenticatedEmployeeId(authentication);

        return ResponseEntity.ok(
                leaveService.getMyLeaveById(id, employeeId)
        );
    }

    // Get leave requests by status
    // Restrict this endpoint to HR and ADMIN in security configuration.
    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getLeavesByStatus(
            @RequestParam LeaveStatus status) {

        return ResponseEntity.ok(
                leaveService.getLeavesByStatus(status)
        );
    }

    // Cancel a pending leave request
    @PutMapping("/{id}/cancel")
    public ResponseEntity<LeaveResponse> cancelLeave(
            @PathVariable UUID id,
            Authentication authentication) {

        UUID employeeId = getAuthenticatedEmployeeId(authentication);

        return ResponseEntity.ok(
                leaveService.cancelLeave(id, employeeId)
        );
    }

    // Read employee UUID from authenticated principal
    private UUID getAuthenticatedEmployeeId(
            Authentication authentication) {

        if (authentication == null
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new LeaveException(
                    "Authenticated employee information is missing"
            );
        }

        try {
            return UUID.fromString(authentication.getName());

        } catch (IllegalArgumentException ex) {

            throw new LeaveException(
                    "JWT principal name must contain a valid employee UUID"
            );
        }
    }
}
