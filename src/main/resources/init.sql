DROP TABLE IF EXISTS videos CASCADE;
DROP TYPE IF EXISTS video_status CASCADE;

CREATE TYPE video_status AS ENUM ('QUEUED', 'PROCESSING', 'DONE');

CREATE TABLE videos (
    id uuid primary key,
    checksum varchar(64) not null unique ,
    input_path text not null,
    output_path text,
    name text not null,
    status video_status not null,
    created_at timestamp not null default now(),
    expires_at timestamp,
    input_size_bytes bigint,
    output_size_bytes bigint,
    download_count int default 0
);
