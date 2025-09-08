package com.memory.Utilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.storage")
@Getter @Setter
public class StorageProperties {
    private String uploadDir;
    private String outputDir;
    private int ttl;
}
