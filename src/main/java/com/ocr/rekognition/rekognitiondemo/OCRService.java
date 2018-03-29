package com.ocr.rekognition.rekognitiondemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public interface OCRService {

    Logger LOGGER = LoggerFactory.getLogger(OCRService.class);

    String detectText(File file) throws OCRException;

    CNH detectCNH(File file) throws OCRException;

    default byte[] bytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.error("Error reading file " + file.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }

}
