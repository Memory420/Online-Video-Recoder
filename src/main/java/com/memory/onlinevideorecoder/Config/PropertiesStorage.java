package com.memory.onlinevideorecoder.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "video")
@Component
@Getter
@Setter
public class PropertiesStorage {
    private String inputDir;
    private String outputDir;
}
