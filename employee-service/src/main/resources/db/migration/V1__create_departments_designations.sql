CREATE TABLE departments (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE designations (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);
