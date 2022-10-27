package com.softkit.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    void fileUpload(MultipartFile file);
}