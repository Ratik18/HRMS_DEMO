package com.example.leave.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.leave.model.LeaveType;

@Repository
<<<<<<< HEAD
public interface LeaveTypeRepository extends JpaRepository<LeaveType, UUID> { 
	

    List<LeaveType> findByActiveTrue();
=======
public interface LeaveTypeRepository extends JpaRepository<LeaveType, UUID> {
>>>>>>> f0e8c18029305a659e6d704edc8ad70600380cb1

    Optional<LeaveType> findByNameIgnoreCase(String name);

}