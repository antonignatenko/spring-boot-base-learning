package com.softkit.controller;

import com.softkit.dto.ImageResponseDTO;
import com.softkit.dto.UserDataDTO;
import com.softkit.model.User;
import com.softkit.repository.UserRepository;
import com.softkit.security.JwtTokenProvider;
import com.softkit.service.ImageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
    @PostMapping("/post")
    public ResponseEntity<ImageResponseDTO> postImage(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "title") String title
    ) throws IOException {
        ImageResponseDTO imageResponseDTO = imageService.postImage(username, file, title);
        return new ResponseEntity<>(imageResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public RedirectView getImageByUserId(@PathVariable Long id) {
        String imageURL = imageService.getImage(id);
        return new RedirectView(imageURL);
    }
}
