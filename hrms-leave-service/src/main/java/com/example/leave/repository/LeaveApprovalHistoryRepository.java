package com.example.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.leave.model.LeaveApprovalHistory;

@Repository
public interface LeaveApprovalHistoryRepository
        extends JpaRepository<LeaveApprovalHistory, Long> {

    // Find approval history for a particular leave request
    List<LeaveApprovalHistory>
        findByLeaveRequestIdOrderByActionAtDesc(Long leaveRequestId);

    // Find all actions performed by an approver
    List<LeaveApprovalHistory>
        findByApproverIdOrderByActionAtDesc(Long approverId);
}

