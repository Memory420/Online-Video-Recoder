package com.memory.Repositories;

import com.memory.Models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findByInputPath(String name);
}
