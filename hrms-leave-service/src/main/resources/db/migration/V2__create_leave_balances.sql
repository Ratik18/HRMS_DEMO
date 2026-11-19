CREATE TABLE leave_management.leave_balances (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    leave_type_id BIGINT NOT NULL,
    total_days NUMERIC(5, 2) NOT NULL,
    used_days NUMERIC(5, 2) NOT NULL DEFAULT 0,
    remaining_days NUMERIC(5, 2) NOT NULL,
    year INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_leave_balance_leave_type
        FOREIGN KEY (leave_type_id)
        REFERENCES leave_management.leave_types(id),

    CONSTRAINT uk_employee_leave_type_year
        UNIQUE (employee_id, leave_type_id, year),

    CONSTRAINT chk_leave_balance_days
        CHECK (
            total_days >= 0
            AND used_days >= 0
            AND remaining_days >= 0
        )
);

CREATE INDEX idx_leave_balance_employee
    ON leave_management.leave_balances(employee_id);