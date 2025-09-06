package com.memory.Models;

import com.memory.Enums.JobStatus;
import com.memory.Enums.PresetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "jobs")
public class Video {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(name = "input_size_bytes")
    private Long inputSizeBytes;

    @Column(name = "output_size_bytes")
    private Long outputSizeBytes;

    @Column(name = "input_path", nullable = false)
    private String inputPath;

    @Column(name = "output_path")
    private String outputPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PresetType preset;

    @Column(nullable = false)
    private String params = "{}";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(length = 64)
    private String checksum;
}
