package com.softkit.service.impl;

import com.softkit.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${sender-email}")
    private static String SENDER_EMAIL;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Async
    public boolean sendEmail(String toEmail, String subject, String body) {
        try{
            logger.info("inside sendEmail method");
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(SENDER_EMAIL);
            mailMessage.setTo(toEmail);
            mailMessage.setText(body);
            mailMessage.setSubject(subject);
            mailSender.send(mailMessage);
            return true;
        }
        catch (RuntimeException e){
            logger.error("fail to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }

    @Override
    public void sendActivationEmail(SimpleMailMessage mailMessage) {
        try {
            logger.info("inside email activation method");
            mailSender.send(mailMessage);
        }
        catch (MailException e) {
            logger.error("failed to send email");
        }
    }
}
