package com.example.leave.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    // Get all Leave Types
    @GetMapping
    public ResponseEntity<List<LeaveType>> getAllLeaveTypes() {

        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        return ResponseEntity.ok(leaveTypes);
    }

    // Create a new Leave Type
    @PostMapping("/create")
    public ResponseEntity<LeaveType> createLeaveType(
            @RequestBody LeaveType leaveType) {

        LeaveType savedLeaveType = leaveTypeRepository.save(leaveType);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedLeaveType);
    }

    // Get Leave Type by ID
    @GetMapping("/{id}")
    public ResponseEntity<LeaveType> getLeaveTypeById(
            @PathVariable Long id) {

        return leaveTypeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update Leave Type
    @PutMapping("/{id}")
    public ResponseEntity<LeaveType> updateLeaveType(
            @PathVariable Long id,
            @RequestBody LeaveType updatedLeaveType) {

        return leaveTypeRepository.findById(id)
                .map(existingLeaveType -> {

                    existingLeaveType.setCode(updatedLeaveType.getCode());
                    existingLeaveType.setName(updatedLeaveType.getName());

                    existingLeaveType.setAccrualRatePerMonth(
                            updatedLeaveType.getAccrualRatePerMonth());

                    existingLeaveType.setMaxCarryForward(
                            updatedLeaveType.getMaxCarryForward());

                    existingLeaveType.setIsPaid(
                            updatedLeaveType.getIsPaid());

                    LeaveType savedLeaveType =
                            leaveTypeRepository.save(existingLeaveType);

                    return ResponseEntity.ok(savedLeaveType);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Leave Type
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveType(
            @PathVariable Long id) {

        if (!leaveTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        leaveTypeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}