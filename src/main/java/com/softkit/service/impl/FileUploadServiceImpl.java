package com.softkit.service.impl;

import com.softkit.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public void fileUpload(MultipartFile file) {
        File filePath = new File("/Users/spartiva/Downloads/fileUploads");
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("something went wrong whilst uploading a file",e);
        }
    }
}
