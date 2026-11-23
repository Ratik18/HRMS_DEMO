
CREATE TABLE leave_management.leave_approval_history (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    leave_request_id UUID NOT NULL,

    approver_id UUID NOT NULL,

    action VARCHAR(20) NOT NULL,

    comment VARCHAR(500),

    action_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_leave_approval_history_request
        FOREIGN KEY (leave_request_id)
        REFERENCES leave_management.leave_requests(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_leave_approval_history_action
        CHECK (action IN ('APPROVED', 'REJECTED'))

);

CREATE INDEX idx_leave_approval_history_request
    ON leave_management.leave_approval_history(leave_request_id);

CREATE INDEX idx_leave_approval_history_approver
    ON leave_management.leave_approval_history(approver_id);
