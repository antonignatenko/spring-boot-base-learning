package com.softkit.dto;

import com.softkit.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {

    private String title;

    private String path;

    private User user;

    private LocalDateTime createdDate;
}
