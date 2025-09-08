package com.memory.Repositories;

import com.memory.Enums.JobStatus;
import com.memory.Models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findByInputPath(String name);

    Video findByChecksum(String checksum);

    List<Video> findVideosByStatusAndExpiresAtBefore(JobStatus status, OffsetDateTime now);
}
