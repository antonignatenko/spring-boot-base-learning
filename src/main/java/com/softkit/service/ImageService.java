package com.softkit.service;

import com.softkit.dto.ImageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ImageResponseDTO postImage(String username, MultipartFile multipartFile, String title);

    String getImage(Integer userId);
}
