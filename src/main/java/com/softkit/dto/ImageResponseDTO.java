package com.softkit.dto;

import com.softkit.controller.model.User;

import java.time.LocalDateTime;

public record ImageResponseDTO
        (Long id, String title, String path,User user, LocalDateTime createdDate) {
}
