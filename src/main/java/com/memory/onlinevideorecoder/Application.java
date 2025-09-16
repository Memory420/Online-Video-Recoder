package com.memory.onlinevideorecoder;

import com.memory.onlinevideorecoder.Config.PropertiesStorage;
import com.memory.onlinevideorecoder.Models.Video;
import com.memory.onlinevideorecoder.Models.VideoStatus;
import com.memory.onlinevideorecoder.Repositories.VideoRepository;
import com.memory.onlinevideorecoder.Utils.FileChecksum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(Application.class);
    private final PropertiesStorage ps;
    private final VideoRepository vr;

    public Application(PropertiesStorage ps, VideoRepository vr) {
        this.ps = ps;
        this.vr = vr;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String wd = System.getProperty("user.dir");
        log.debug("Working dir = {}{}", wd, ps.getInputDir());

        Path videoPath = Paths.get("/home/scotium/IdeaProjects/Online-Video-Recoder/input/video.mp4");
        Video video = new Video();
        video.setInputSizeBytes(Files.size(videoPath));
        video.setInputPath(videoPath);
        video.setChecksum(FileChecksum.calculateSHA256(videoPath));
        video.setName(FilenameUtils.getBaseName(videoPath.toString()));
        video.setCreatedAt(LocalDateTime.now());
        video.setStatus(VideoStatus.QUEUED);
        vr.save(video);
    }
}
