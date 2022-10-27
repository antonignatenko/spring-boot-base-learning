package com.softkit.controller;

import com.softkit.dto.ImageResponseDTO;
import com.softkit.service.FileUploadService;
import com.softkit.service.ImageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@Api(tags = "images")
@EnableWebSecurity
public class ImageController implements WebMvcConfigurer {
    private final ImageService imageService;
    private final FileUploadService uploadService;

    @PostMapping("/post")
    public ResponseEntity<ImageResponseDTO> postImage(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "title") String title
    ) throws IOException {
        ImageResponseDTO imageResponseDTO = imageService.postImage(username, file, title);
        return ResponseEntity.ok(imageResponseDTO);
    }

    @GetMapping("/{id}")
    public RedirectView getImageByUserId(@PathVariable Integer id) {
        String imageURL = imageService.getImage(id);
        return new RedirectView(imageURL);
    }

    @PostMapping("/upload")
    public void uploadPhoto(@RequestParam("file") MultipartFile file) {
        uploadService.fileUpload(file);
    }
}
