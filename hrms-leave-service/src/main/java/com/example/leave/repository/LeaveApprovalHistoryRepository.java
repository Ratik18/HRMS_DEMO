package com.example.leave.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.leave.model.LeaveApprovalHistory;

@Repository
public interface LeaveApprovalHistoryRepository
        extends JpaRepository<LeaveApprovalHistory, UUID> {

    // Find approval history for a particular leave request
    List<LeaveApprovalHistory>
        findByLeaveRequestIdOrderByActionAtDesc(UUID leaveRequestId);

    // Find all actions performed by an approver
    List<LeaveApprovalHistory>
        findByApproverIdOrderByActionAtDesc(UUID approverId);
}

