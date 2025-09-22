package com.memory.onlinevideorecoder;

import com.memory.onlinevideorecoder.Config.PropertiesStorage;
import com.memory.onlinevideorecoder.Repositories.VideoRepository;
import com.memory.onlinevideorecoder.Services.VideoRecodering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(Application.class);
    private final VideoRecodering videoRecodering;
    private final PropertiesStorage ps;
    private final VideoRepository vr;

    public Application(VideoRecodering videoRecodering, PropertiesStorage ps, VideoRepository vr) {
        this.videoRecodering = videoRecodering;
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
//        log.debug(String.valueOf(videoRecodering.
//                processVideo(Paths.
//                        get("/home/scotium/IdeaProjects/Online-Video-Recoder/input/video.mp4"), "p3", 20)));
    }
}
