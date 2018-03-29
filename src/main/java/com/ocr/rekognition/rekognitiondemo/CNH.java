package com.ocr.rekognition.rekognitiondemo;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class CNH {

    private final String name;
    private final String rg;
    private final String cnh;
    private final LocalDate dob;
    private final String parents;
    private final LocalDate doe;

}
