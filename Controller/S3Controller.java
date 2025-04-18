package com.indivar.edubold3.Fee.S3.Controller;

import com.indivar.edubold3.Fee.FeeReceipt.Service.FeeReceiptService;
import com.indivar.edubold3.Fee.S3.Service.S3ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.indivar.edubold3.UniversalKit.ApplicationUrls.ControllerUrls.S3_URL;

@RestController
@RequestMapping(S3_URL)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class S3Controller {

    private final S3ImageUploader imageUploader;

    public S3Controller(S3ImageUploader imageUploader) {
        super();
        this.imageUploader = imageUploader;
    }

    //upload image
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file)
    {
        return ResponseEntity.ok(imageUploader.uploadFile(file));
    }

}
