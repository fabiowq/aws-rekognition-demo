package com.ocr.rekognition.rekognitiondemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;

@SpringBootApplication
@PropertySource("classpath:secure.yml")
public class RekognitionDemoApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(RekognitionDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RekognitionDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(FileLoader fileLoader, OCRService ocrService) {
		return args -> {
            fileLoader.list().stream().forEach(
                file -> {
                    LOGGER.info("File={}", file.getAbsolutePath());
                    String text = detectText(file, ocrService);
                    LOGGER.info("Text={}", text);
                });
        };
	}

    private String detectText(File file, OCRService ocrService) {
        try {
            return ocrService.detectText(file);
        } catch (OCRException e) {
            LOGGER.error("Error detecting text from file " + file.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }

}
