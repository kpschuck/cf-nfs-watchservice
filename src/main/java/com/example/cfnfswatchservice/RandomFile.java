package com.example.cfnfswatchservice;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
class RandomFile {
    private Boolean generated = false;
    private String directory;

    RandomFile() {}

    RandomFile(String directory) {
        this.directory = directory;
    }

    public boolean isGenerated() {
        return this.generated;
    }

    public void generate() throws IOException {
        String fileName = UUID.randomUUID().toString();
        Path filePath = Paths.get(directory, fileName);
        File randomFile = new File(filePath.toString());
        if (randomFile.createNewFile()) {
            log.info("File created: " + randomFile.getAbsolutePath());
        } else {
            log.warn("File " + randomFile.getAbsolutePath() + " already exists.");
        }
        this.generated = true;
    }
}
