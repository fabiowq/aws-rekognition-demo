package com.ocr.rekognition.rekognitiondemo;

public class OCRException extends Exception {

    public OCRException() {
    }

    public OCRException(String s) {
        super(s);
    }

    public OCRException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OCRException(Throwable throwable) {
        super(throwable);
    }

    public OCRException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
