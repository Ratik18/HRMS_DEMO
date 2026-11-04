package com.hrms.employee.service;

import com.hrms.employee.model.Designation;
import com.hrms.employee.repository.DesignationRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationService {

    private final DesignationRepository designationRepository;

    public DesignationService(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    public Designation createDesignation(Designation designation) {

        if (designationRepository.existsByTitleIgnoreCase(
                designation.getTitle())) {
            throw new RuntimeException("Designation already exists");
        }

        return designationRepository.save(designation);
    }

    public List<Designation> getAllDesignations() {
        return designationRepository.findAll();
    }

    public Designation getDesignationById(Long id) {
        return designationRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Designation not found: " + id));
    }

    public Designation updateDesignation(Long id, Designation updated) {

        Designation existing = getDesignationById(id);

        if (!existing.getTitle().equalsIgnoreCase(updated.getTitle())
                && designationRepository.existsByTitleIgnoreCase(
                        updated.getTitle())) {
            throw new RuntimeException("Designation already exists");
        }

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());

        return designationRepository.save(existing);
    }

    public void deleteDesignation(Long id) {
        Designation designation = getDesignationById(id);
        designationRepository.delete(designation);
    }
}