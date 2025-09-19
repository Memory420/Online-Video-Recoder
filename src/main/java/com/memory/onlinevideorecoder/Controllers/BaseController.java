package com.memory.onlinevideorecoder.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BaseController {

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
    ) {
        String msg = String.format("File: %s<br>Preset: %s<br>QP: %s",
                file.getOriginalFilename(), preset, qp);
        model.addAttribute("message", msg);
        return "result";
    }
}

