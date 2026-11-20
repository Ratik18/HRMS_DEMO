
CREATE TABLE leave_management.leave_requests (

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    employee_id UUID NOT NULL,

    leave_type_id UUID NOT NULL,

    start_date DATE NOT NULL,

    end_date DATE NOT NULL,

    total_days NUMERIC(5, 2) NOT NULL,

    reason VARCHAR(500) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    approver_id UUID,

    decision_comment VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_leave_request_type
        FOREIGN KEY (leave_type_id)
        REFERENCES leave_management.leave_types(id),

    CONSTRAINT chk_leave_request_dates
        CHECK (end_date >= start_date),

    CONSTRAINT chk_leave_request_days
        CHECK (total_days > 0),

    CONSTRAINT chk_leave_request_status
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED'))

);

CREATE INDEX idx_leave_requests_employee
    ON leave_management.leave_requests(employee_id);

CREATE INDEX idx_leave_requests_status
    ON leave_management.leave_requests(status);

CREATE INDEX idx_leave_requests_dates
    ON leave_management.leave_requests(start_date, end_date);
