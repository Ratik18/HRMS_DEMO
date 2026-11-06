package com.hrms.employee.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hrms.employee.model.Designation;
import com.hrms.employee.service.DesignationService;

@RestController
@RequestMapping("/api/designations")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    // Create Designation
    @PostMapping
    public ResponseEntity<Designation> createDesignation(
            @RequestBody Designation designation) {

        Designation saved =
                designationService.createDesignation(designation);

        return new ResponseEntity<>(
                saved, HttpStatus.CREATED);
    }

    // Get All Designations
    @GetMapping
    public ResponseEntity<List<Designation>> getAllDesignations() {

        List<Designation> designations =
                designationService.getAllDesignations();

        return ResponseEntity.ok(designations);
    }

    // Get Designation By ID
    @GetMapping("/{id}")
    public ResponseEntity<Designation> getDesignationById(
            @PathVariable Long id) {

        Designation designation =
                designationService.getDesignationById(id);

        return ResponseEntity.ok(designation);
    }

    // Update Designation
    @PutMapping("/{id}")
    public ResponseEntity<Designation> updateDesignation(
            @PathVariable Long id,
            @RequestBody Designation designation) {

        Designation updated =
                designationService.updateDesignation(id, designation);

        return ResponseEntity.ok(updated);
    }

    // Delete Designation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesignation(
            @PathVariable Long id) {

        designationService.deleteDesignation(id);

        return ResponseEntity.noContent().build();
    }
}