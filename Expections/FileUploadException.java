package com.indivar.edubold3.Fee.S3.Expections;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String errorMessage) {
        super(errorMessage);
    }
}
