CREATE TABLE hrms.onboarding_checklists (

    id BIGSERIAL PRIMARY KEY,

    employee_id BIGINT NOT NULL,

    task_name VARCHAR(200) NOT NULL,

    description TEXT,

    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_onboarding_employee
        FOREIGN KEY (employee_id)
        REFERENCES hrms.employees(id),

    CONSTRAINT chk_onboarding_status
        CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED'))
);

CREATE INDEX idx_onboarding_employee
    ON hrms.onboarding_checklists(employee_id);