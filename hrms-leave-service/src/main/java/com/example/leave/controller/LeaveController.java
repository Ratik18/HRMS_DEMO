package com.example.leave.controller;

import java.util.List;

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
import com.example.leave.model.LeaveBalance;
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

        Long employeeId = getAuthenticatedEmployeeId(authentication);

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

        Long employeeId = getAuthenticatedEmployeeId(authentication);

        return ResponseEntity.ok(
                leaveService.getMyLeaves(employeeId)
        );
    }

    // Get logged-in employee's leave balance
    @GetMapping("/balance")
    public ResponseEntity<List<LeaveBalance>> getMyLeaveBalance(
            Authentication authentication) {

        Long employeeId = getAuthenticatedEmployeeId(authentication);

        return ResponseEntity.ok(
                leaveService.getMyLeaveBalance(employeeId)
        );
    }

    // Get one leave request belonging to the logged-in employee
    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponse> getMyLeaveById(
            @PathVariable Long id,
            Authentication authentication) {

        Long employeeId = getAuthenticatedEmployeeId(authentication);

        return ResponseEntity.ok(
                leaveService.getMyLeaveById(id, employeeId)
        );
    }

    // Get leave requests by status
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
            @PathVariable Long id,
            Authentication authentication) {

        Long employeeId = getAuthenticatedEmployeeId(authentication);

        return ResponseEntity.ok(
                leaveService.cancelLeave(id, employeeId)
        );
    }

    // Get employee ID using JWT email
    private Long getAuthenticatedEmployeeId(
            Authentication authentication) {

        if (authentication == null
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new LeaveException(
                    "Authenticated employee information is missing"
            );
        }

        String email = authentication.getName();

        return leaveService.getEmployeeIdByEmail(email);
    }
}