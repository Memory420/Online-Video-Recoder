CREATE TYPE job_status AS ENUM ('QUEUED', 'RUNNING', 'SUCCEEDED');

CREATE TYPE preset_type AS ENUM ('CUSTOM', 'LOW', 'HIGH');

CREATE TABLE jobs (
    id UUID PRIMARY KEY,
    status job_status NOT NULL,
    input_size_bytes BIGINT,
    output_size_bytes BIGINT,
    input_path TEXT NOT NULL,
    output_path TEXT,
    preset preset_type NOT NULL,
    params TEXT NOT NULL DEFAULT '{}',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    started_at TIMESTAMPTZ,
    expires_at TIMESTAMPTZ,
    download_count INT DEFAULT 0,
    checksum CHAR(64)
);

CREATE INDEX idx_jobs_checksum ON jobs (checksum);

CREATE INDEX idx_jobs_expires_at ON jobs (expires_at);