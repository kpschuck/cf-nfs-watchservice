package com.example.cfnfswatchservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RestController
public class RandomFileController {
    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping("/generate")
    RandomFile newRandomFile() throws IOException {
        cleanup();
        RandomFile randomFile = new RandomFile(uploadDir);
        randomFile.generate();
        return randomFile;
    }

    private void cleanup() throws IOException {
        if (Objects.requireNonNull(new File(uploadDir).list()).length > 50) {
            FileUtils.cleanDirectory(new File(uploadDir));
        }
    }
}
