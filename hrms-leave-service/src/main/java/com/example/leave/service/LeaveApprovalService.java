//package com.example.leave.service;
//
//import java.math.BigDecimal;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.example.leave.dto.LeaveDecisionRequest;
//import com.example.leave.dto.LeaveResponse;
//import com.example.leave.exception.LeaveException;
//import com.example.leave.model.LeaveApprovalHistory;
//import com.example.leave.model.LeaveRequest;
//import com.example.leave.model.LeaveStatus;
//import com.example.leave.repository.LeaveApprovalHistoryRepository;
//import com.example.leave.repository.LeaveRequestRepository;
//
//@Service
//@Transactional
//public class LeaveApprovalService {
//
//    private final LeaveRequestRepository leaveRequestRepository;
//
//    private final LeaveApprovalHistoryRepository
//            leaveApprovalHistoryRepository;
//
//    private final LeaveBalanceService leaveBalanceService;
//
//    public LeaveApprovalService(
//            LeaveRequestRepository leaveRequestRepository,
//            LeaveApprovalHistoryRepository leaveApprovalHistoryRepository,
//            LeaveBalanceService leaveBalanceService) {
//
//        this.leaveRequestRepository = leaveRequestRepository;
//        this.leaveApprovalHistoryRepository =
//                leaveApprovalHistoryRepository;
//        this.leaveBalanceService = leaveBalanceService;
//    }
//
//    // Approve a leave request
//    public LeaveResponse approveLeave(
//            Long leaveId,
//            Long approverId,
//            LeaveDecisionRequest request) {
//
//        return processDecision(
//                leaveId,
//                approverId,
//                request,
//                LeaveStatus.APPROVED
//        );
//    }
//
//    // Reject a leave request
//    public LeaveResponse rejectLeave(
//            Long leaveId,
//            Long approverId,
//            LeaveDecisionRequest request) {
//
//        return processDecision(
//                leaveId,
//                approverId,
//                request,
//                LeaveStatus.REJECTED
//        );
//    }
//
//    // Common method for approval and rejection
//    private LeaveResponse processDecision(
//            Long leaveId,
//            Long approverId,
//            LeaveDecisionRequest request,
//            LeaveStatus decision) {
//
//        if (leaveId == null || leaveId <= 0) {
//            throw new LeaveException("Invalid leave request ID");
//        }
//
//        if (approverId == null || approverId <= 0) {
//            throw new LeaveException("Invalid approver ID");
//        }
//
//        if (request == null || request.comment() == null
//                || request.comment().isBlank()) {
//            throw new LeaveException("Decision comment is required");
//        }
//
//        // Lock the request to prevent simultaneous decisions
//        LeaveRequest leaveRequest = leaveRequestRepository
//                .findByIdForUpdate(leaveId)
//                .orElseThrow(() -> new LeaveException(
//                        "Leave request not found"
//                ));
//
//        // Only pending requests can be approved or rejected
//        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
//            throw new LeaveException(
//                    "Only pending leave requests can be processed"
//            );
//        }
//
//        // Deduct balance only when approving
//        if (decision == LeaveStatus.APPROVED) {
//
//            leaveBalanceService.deductBalance(
//                    leaveRequest.getEmployeeId(),
//                    leaveRequest.getLeaveTypeId(),
//                    leaveRequest.getTotalDays()
//            );
//        }
//
//        // Update the leave request
//        leaveRequest.setStatus(decision);
//        leaveRequest.setApproverId(approverId);
//        leaveRequest.setDecisionComment(request.comment());
//
//        LeaveRequest updatedRequest =
//                leaveRequestRepository.save(leaveRequest);
//
//        // Record approval/rejection history
//        LeaveApprovalHistory history = new LeaveApprovalHistory();
//
//        history.setLeaveRequestId(updatedRequest.getId());
//        history.setApproverId(approverId);
//        history.setAction(decision.name());
//        history.setComment(request.comment());
//
//        leaveApprovalHistoryRepository.save(history);
//
//        return convertToResponse(updatedRequest);
//    }
//
//    // Convert entity into response DTO
//    private LeaveResponse convertToResponse(
//            LeaveRequest leaveRequest) {
//
//        return new LeaveResponse(
//                leaveRequest.getId(),
//                leaveRequest.getEmployeeId(),
//                leaveRequest.getLeaveTypeId(),
//                leaveRequest.getStartDate(),
//                leaveRequest.getEndDate(),
//                leaveRequest.getTotalDays(),
//                leaveRequest.getReason(),
//                leaveRequest.getStatus(),
//                leaveRequest.getApproverId(),
//                leaveRequest.getDecisionComment(),
//                leaveRequest.getCreatedAt(),
//                leaveRequest.getUpdatedAt()
//        );
//    }
//}
//
