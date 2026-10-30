CREATE TABLE employees (
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    phone           VARCHAR(20),
    date_of_joining DATE NOT NULL,
    salary          NUMERIC(12,2),
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    department_id   BIGINT NOT NULL REFERENCES departments(id),
    designation_id  BIGINT NOT NULL REFERENCES designations(id),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_employees_department  ON employees(department_id);
CREATE INDEX idx_employees_designation ON employees(designation_id);
