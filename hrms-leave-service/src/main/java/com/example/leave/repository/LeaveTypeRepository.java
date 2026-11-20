package com.example.leave.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.leave.model.LeaveType;


@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, UUID> { 
	

    List<LeaveType> findByActiveTrue();

    Optional<LeaveType> findByNameIgnoreCase(String name);
	
	
	
	

}
