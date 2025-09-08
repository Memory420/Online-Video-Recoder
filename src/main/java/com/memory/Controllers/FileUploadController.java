package com.memory.Controllers;

import com.memory.Enums.JobStatus;
import com.memory.Enums.PresetType;
import com.memory.Models.Video;
import com.memory.Repositories.VideoRepository;
import com.memory.Utilities.HashUtilities;
import com.memory.Utilities.StorageProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;

@Controller
public class FileUploadController {

    private final VideoRepository videoRepository;
    private final String UPLOAD_DIR;
    private final int ttl;

    public FileUploadController(VideoRepository videoRepository, StorageProperties storageProperties) {
        this.videoRepository = videoRepository;
        this.UPLOAD_DIR = System.getProperty("user.dir") + storageProperties.getUploadDir();
        this.ttl = storageProperties.getTtl();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Файл пустой");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.endsWith(".mp4") || fileName.endsWith(".avi"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Разрешены только .mp4 и .avi");
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            System.out.println(UPLOAD_DIR + filePath.getFileName());
            file.transferTo(filePath.toFile());

            OffsetDateTime odt = OffsetDateTime.now();
            Video video = new Video();
            video.setStatus(JobStatus.SUCCEEDED);
            video.setInputSizeBytes((long) file.getBytes().length);
            video.setInputPath(filePath.toString());
            video.setPreset(PresetType.CUSTOM);
            video.setCreatedAt(odt);
            video.setChecksum(HashUtilities.sha256(filePath));
            video.setExpiresAt(odt.plusSeconds(ttl));

            videoRepository.save(video);

            return ResponseEntity.ok("Файл загружен: " + fileName);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка загрузки: " + e.getMessage());
        }
    }
}
