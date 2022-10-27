package com.softkit.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    boolean sendEmail(String toEmail, String subject, String body);

    void sendActivationEmail(SimpleMailMessage mailMessage);
}
