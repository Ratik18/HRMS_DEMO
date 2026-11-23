package com.example.leave.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave.model.LeaveType;
import com.example.leave.repository.LeaveTypeRepository;

@RestController
@RequestMapping("/api/leave-types")
public class LeaveTypeController {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeController(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    @GetMapping
    public ResponseEntity<List<LeaveType>> getAllLeaveTypes() {

        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        return ResponseEntity.ok(leaveTypes);
    }
}