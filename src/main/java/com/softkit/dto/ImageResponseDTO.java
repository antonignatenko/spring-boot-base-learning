package com.softkit.dto;

import com.softkit.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDTO {

    private Long id;

    private String title;

    private String path;

    private User user;

    private LocalDateTime createdDate;
}
