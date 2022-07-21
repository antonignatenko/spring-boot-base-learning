package com.softkit.controller;


import com.softkit.dto.ImageResponseDTO;
import com.softkit.dto.UserDataDTO;
import com.softkit.dto.UserResponseDTO;
import com.softkit.service.ImageService;
import com.softkit.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@Api(tags = "images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/post")
    public ResponseEntity<ImageResponseDTO> postImage(
            UserDataDTO userDto,
            @RequestParam MultipartFile file,
            @RequestParam(value = "title") String title
    ) throws IOException {
        return ResponseEntity.created(URI.create("/community/images"))
                .body(imageService.postImage(userDto, file, title));
    }

    @GetMapping()
    public String getImageByUserId(@PathVariable Long id) {
        return imageService.getImage(id);
    }
}
