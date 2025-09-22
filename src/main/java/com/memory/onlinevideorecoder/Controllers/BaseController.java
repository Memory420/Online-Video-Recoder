package com.memory.onlinevideorecoder.Controllers;

import com.memory.onlinevideorecoder.Config.PropertiesStorage;
import com.memory.onlinevideorecoder.Models.Video;
import com.memory.onlinevideorecoder.Models.VideoStatus;
import com.memory.onlinevideorecoder.Repositories.VideoRepository;
import com.memory.onlinevideorecoder.Utils.FileChecksum;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Controller
public class BaseController {
    private final Logger log = LoggerFactory.getLogger(BaseController.class);
    private final VideoRepository vr;
    private final String uploadDir;


    public BaseController(VideoRepository vr, PropertiesStorage ps) {
        this.vr = vr;
        this.uploadDir = ps.getInputDir();
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/upload")
    public String handleUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("preset") String preset,
            @RequestParam("qp") int qp,
            Model model
    ) throws IOException, NoSuchAlgorithmException {
        Path inputDir = Paths.get(System.getProperty("user.dir") + uploadDir);
        log.debug("inputDir: {}", inputDir);
        if (!Files.exists(inputDir)) {
            Files.createDirectories(inputDir);
        }
        Path target = inputDir.resolve(file.getOriginalFilename());
        log.debug("target: {}", target);
        file.transferTo(target);


        Video video = new Video(); // ручное тестирование
        video.setInputSizeBytes(Files.size(target));
        video.setInputPath(target);
        video.setChecksum(FileChecksum.calculateSHA256(target));
        video.setName(FilenameUtils.getBaseName(target.toString()));
        video.setCreatedAt(LocalDateTime.now());
        video.setStatus(VideoStatus.QUEUED);
        vr.save(video);

        String msg = String.format("File: %s<br>Preset: %s<br>QP: %s",
                file.getOriginalFilename(), preset, qp);

        log.debug(msg);

        model.addAttribute("message", msg);
        return "result";
    }
}

