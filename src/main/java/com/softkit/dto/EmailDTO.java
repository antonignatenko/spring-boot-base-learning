package com.softkit.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class EmailDTO {
    private String subject;

    private String body;

    private String toEmail;
}
