CREATE TABLE payroll_profiles (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL UNIQUE,
    base_salary NUMERIC(12, 2) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE payroll_adjustments (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL,
    leave_request_id UUID NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    reason VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_payroll_adjustments_employee ON payroll_adjustments (employee_id);
CREATE INDEX idx_payroll_adjustments_leave_request ON payroll_adjustments (leave_request_id);
