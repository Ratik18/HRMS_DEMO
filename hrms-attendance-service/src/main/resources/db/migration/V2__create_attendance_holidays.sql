
CREATE TABLE attendance.attendance_holidays (

    id BIGSERIAL PRIMARY KEY,

    holiday_date DATE NOT NULL,

    holiday_name VARCHAR(150) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_attendance_holiday_date
        UNIQUE (holiday_date)
);
