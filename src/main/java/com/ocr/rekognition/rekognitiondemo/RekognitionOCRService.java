package com.ocr.rekognition.rekognitiondemo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class RekognitionOCRService implements OCRService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RekognitionOCRService.class);

    private final String accessKeyId;
    private final String secretAccessKey;
    private final String outputDir;

    public RekognitionOCRService(
            @Value("${aws.access.key.id}") String accessKeyId,
            @Value("${aws.secret.access.key}") String secretAccessKey,
            @Value("${output.dir}") String outputDir) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.outputDir = outputDir;
    }

    @Override
    public CNH detectCNH(File file) throws OCRException {
        DetectTextResult result = detectText(bytes(file));
        CNH cnh = CNH.builder().build();
        return cnh;
    }

    @Override
    public String detectText(File file) throws OCRException {
        DetectTextResult result = detectText(bytes(file));
        log(file, result);
        String detectedText = result.getTextDetections().stream().map(td -> td.getDetectedText()).collect(Collectors.joining("\n"));
        return detectedText;
    }

    private DetectTextResult detectText(byte[] fileBytes) throws OCRException {

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .build();

        DetectTextRequest request = new DetectTextRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(fileBytes)));

        try {
            return rekognitionClient.detectText(request);
        } catch(AmazonRekognitionException e) {
            throw new OCRException(e);
        }

    }

    private void log(File file, DetectTextResult result) {
        String outputFileName = FilenameUtils.concat(
                outputDir,
                FilenameUtils.getBaseName(file.getName()) + "_" + System.currentTimeMillis() + ".txt"
        );
        try {
            Files.write(Paths.get(outputFileName), result.getTextDetections().stream().map(
                    td -> {
                        return new StringJoiner("\n")
                                .add("Detected Text: " + td.getDetectedText())
                                .add("Confidence: " + td.getConfidence())
                                .add("Id: " + td.getId())
                                .add("Parent Id: " + td.getParentId())
                                .add("Type: " + td.getType())
                                .add("Geometry: " + td.getGeometry()).add("").toString();
                    }
            ).collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
