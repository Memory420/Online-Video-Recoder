package com.memory.Schedule;

import com.memory.Enums.JobStatus;
import com.memory.Models.Video;
import com.memory.Repositories.VideoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class VideoCleaning {
    private final VideoRepository videoRepository;

    public VideoCleaning(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Scheduled(fixedRate = 15000)
    public void cleanExpiredVideos(){
        List<Video> expired = videoRepository.findVideosByStatusAndExpiresAtBefore(
                JobStatus.SUCCEEDED,
                OffsetDateTime.now()
        );

        for (Video v : expired){
            try {
                Files.deleteIfExists(Path.of(v.getInputPath()));
                v.setStatus(JobStatus.DELETED);
                System.out.println("Статус видео изменён на DELETED");
                videoRepository.save(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
