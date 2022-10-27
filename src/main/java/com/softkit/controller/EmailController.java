package com.softkit.controller;

import com.softkit.dto.EmailDTO;
import com.softkit.service.impl.EmailServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final EmailServiceImpl senderService;

    public EmailController(EmailServiceImpl senderService) {
        this.senderService = senderService;
    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody EmailDTO emailDTO) {
        senderService.sendEmail(emailDTO.toEmail(), emailDTO.subject(), emailDTO.body());
    }
}
