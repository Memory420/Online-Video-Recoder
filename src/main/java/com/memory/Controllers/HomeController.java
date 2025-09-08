package com.memory.Controllers;

import com.memory.Models.Video;
import com.memory.Repositories.VideoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    private final VideoRepository videoRepository;

    public HomeController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @GetMapping("/")
    public String home() {
        return "home.html";
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getVideo(@PathVariable String id){
        Video video = videoRepository.findByChecksum(id);
        if (video == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such video!");
        return ResponseEntity.ok(video.getStatus().toString());
    }
}
