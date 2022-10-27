package com.softkit.service;

import com.softkit.controller.model.ConfirmationToken;

public interface ConfirmationTokenService {

    ConfirmationToken saveToken(ConfirmationToken token);

    ConfirmationToken findByConfirmationToken(String token);
}
