package com.memory.onlinevideorecoder.Services;

import com.memory.onlinevideorecoder.Config.PropertiesStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoRecodering {
    private final Logger log = LoggerFactory.getLogger(VideoRecodering.class);
    private final String outputDir;

    static List<String> commandForFrames = List.of(
            "ffprobe",
            "-v", "error",
            "-count_frames",
            "-select_streams", "v:0",
            "-show_entries", "stream=nb_read_frames",
            "-of", "default=nokey=1:noprint_wrappers=1"
    );

    static List<String> commandForConvert = List.of(
            "ffmpeg",
            "-i", "address",
            "-c:v", "h264_nvenc",
            "-preset", "pp",
            "-cq", "qp",
            "-b:v", "0",
            "-c:a", "copy",
            "output"
    );

    public VideoRecodering(PropertiesStorage ps) {
        outputDir = ps.getOutputDir();
    }

    public boolean processVideo(Path video, String preset, int qp) throws IOException, InterruptedException {
        if (!Files.exists(video)) {
            return false;
        }
        if (preset.isEmpty()){
            return false;
        }

        int totalFrames = getVideoFrameCount(video);
        log.debug("totalFrames: {}", totalFrames);

        List<String> cmd = new ArrayList<>(commandForConvert);

        cmd.replaceAll(arg -> switch (arg) {
            case "address" -> video.toString();
            case "pp" -> preset;
            case "qp" -> String.valueOf(qp);
            case "output" -> Paths.get(System.getProperty("user.dir")
                    + outputDir)
                    + "/"
                    + video.getFileName();
            default -> arg;
        });

        log.debug("commandForConvert: {}", String.join(" ", cmd));

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.debug(line);
            }
        }

        int exitCode = process.waitFor();

        return exitCode == 0;
    }
    public int getVideoFrameCount(Path video) throws IOException, InterruptedException {
        log.debug("Начало getVideoFrameCount();");
        List<String> cmd = new ArrayList<>(commandForFrames);
        cmd.add(video.toString());

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        int frames = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        frames = Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        log.warn("Не удалось распарсить ffprobe output: {}", line);
                    }
                }
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("ffprobe завершился с ошибкой, код: " + exitCode);
        }

        return frames;
    }
}
