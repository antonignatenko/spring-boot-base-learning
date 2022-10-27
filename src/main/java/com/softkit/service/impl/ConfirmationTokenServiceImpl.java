package com.softkit.service.impl;

import com.softkit.controller.model.ConfirmationToken;
import com.softkit.repository.ConfirmationTokenRepository;
import com.softkit.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private static final Logger logger = LoggerFactory.getLogger(ConfirmationTokenServiceImpl.class);
    @Autowired
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public ConfirmationToken saveToken(ConfirmationToken token) {
      return confirmationTokenRepository.save(token);
    }
    @Override
    public ConfirmationToken findByConfirmationToken(String token) {
        try {
            return confirmationTokenRepository.findByConfirmationToken(token);
        } catch (Exception e) {
            logger.error("couldn't find such token");
            throw new RuntimeException("something went wrong");
        }
    }
}
