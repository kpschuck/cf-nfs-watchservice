package com.example.cfnfswatchservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
@Slf4j
public class UploadWatcherService {

    @Value("${upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void watchUpload() throws IOException, InterruptedException {
        ensureUploadDir();
        Runnable watch = () -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();

                Path path = Paths.get(uploadDir);
                path.register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                WatchKey key;
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        log.info("Event kind: " + event.kind()
                                + " | File affected: " + event.context());
                    }
                    key.reset();
                }
            } catch (IOException | InterruptedException e) {
                log.error(e.getMessage());
            }
        };
        new Thread(watch).start();
    }

    private void ensureUploadDir() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            if (directory.mkdir()) {
                log.info("Upload directory created: " + uploadDir);
            } else {
                log.info("Upload directory already exists: " + uploadDir);
            }
        }
    }
}
