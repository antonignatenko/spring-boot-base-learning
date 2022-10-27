package com.softkit.dto;

import com.softkit.controller.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private int birthDay;

    private LocalDateTime registrationDate;

    private List<Role> roles;

    private String UUID;
}
