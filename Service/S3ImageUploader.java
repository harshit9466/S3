package com.indivar.edubold3.Fee.S3.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface S3ImageUploader
{
    String uploadFile(MultipartFile file);

    String uploadFile(String fileFolderName, String fileName, ByteArrayInputStream inputStream);

    List<String> allFiles();

    String preSignedUrl(String key);
}
