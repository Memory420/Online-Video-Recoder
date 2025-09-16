package com.memory.onlinevideorecoder.Repositories;

import com.memory.onlinevideorecoder.Models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
    List<Video> findVideoByName(String name);

    Video findVideoByInputPath(String inputPath);
}
