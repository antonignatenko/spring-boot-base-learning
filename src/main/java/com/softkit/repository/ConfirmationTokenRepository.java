package com.softkit.repository;

import com.softkit.controller.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;


public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}