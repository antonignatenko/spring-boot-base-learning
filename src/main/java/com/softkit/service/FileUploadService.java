package com.softkit.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {
    public void fileUpload(MultipartFile file) {
        File filePath = new File("/Users/antonignatenko/Downloads/fileUploads");
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("something went wrong whilst uploading a file",e);
        }
    }
}
