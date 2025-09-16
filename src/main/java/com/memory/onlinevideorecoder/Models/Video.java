package com.memory.onlinevideorecoder.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("JpaAttributeTypeInspection")
@Data
@Table(name = "videos")
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 64, nullable = false, unique = true)
    private String checksum;

    @Column(name = "input_path", nullable = false)
    private Path inputPath;

    @Column(name = "output_path")
    private Path outputPath;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VideoStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private Long inputSizeBytes;

    private Long outputSizeBytes;

    private int downloadCount;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.downloadCount = 0;
    }
}
