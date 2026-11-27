
CREATE SCHEMA IF NOT EXISTS attendance;

CREATE TABLE attendance.attendance_records (

    id BIGSERIAL PRIMARY KEY,

    employee_id BIGINT NOT NULL,

    attendance_date DATE NOT NULL,

    check_in TIMESTAMP,

    check_out TIMESTAMP,

    status VARCHAR(30) NOT NULL,

    remarks VARCHAR(500),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_employee_attendance_date
        UNIQUE (employee_id, attendance_date),

    CONSTRAINT chk_attendance_status
        CHECK (
            status IN (
                'PRESENT',
                'ABSENT',
                'HALF_DAY',
                'ON_LEAVE',
                'HOLIDAY',
                'WEEK_OFF'
            )
        ),

    CONSTRAINT chk_attendance_time
        CHECK (
            check_in IS NULL
            OR check_out IS NULL
            OR check_out >= check_in
        )
);

CREATE INDEX idx_attendance_employee
ON attendance.attendance_records(employee_id);

CREATE INDEX idx_attendance_date
ON attendance.attendance_records(attendance_date);
