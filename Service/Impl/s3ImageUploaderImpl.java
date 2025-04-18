package com.indivar.edubold3.Fee.S3.Service.Impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.indivar.edubold3.Fee.S3.Expections.FileUploadException;
import com.indivar.edubold3.Fee.S3.Service.S3ImageUploader;
import com.indivar.edubold3.UniversalKit.BasePackage.Exceptions.Request.NotNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class s3ImageUploaderImpl implements S3ImageUploader {
    @Autowired
    private AmazonS3 client;
    @Value("${app.s3.bucket}")
    private String bucketName;
    @Value("${app.s3.base-folder}")
    private String baseFolder;

    @Override
    public String uploadFile(MultipartFile file) {

        if (file == null) {
            throw new NotNullException("File");
        }

        String actualFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + actualFileName.substring(actualFileName.lastIndexOf("."));

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(file.getSize());

        try {
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName, "uploads/" + fileName, file.getInputStream(), metaData));
            return fileName;
        } catch (IOException e) {
            throw new FileUploadException("Error in uploading file: - " + e.getMessage());
        }
    }

    @Override
    public String uploadFile(String fileFolderName, String fileName, ByteArrayInputStream inputStream) {

//        String fileName = UUID.randomUUID().toString();

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(inputStream.available());

        String s3Key = String.format("%s/%s/%s",
                baseFolder,
                fileFolderName,
                fileName);

        PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName, s3Key, inputStream, metaData));
        return fileName;
    }


    @Override
    public List<String> allFiles() {
        return List.of();
    }

    @Override
    public String preSignedUrl(String key) {
        Date expirationDate = new Date();
        long time = expirationDate.getTime();
        int hour = 24;
        time = time + (hour * 60 * 1000);
        expirationDate.setTime(time);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expirationDate);
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

}
