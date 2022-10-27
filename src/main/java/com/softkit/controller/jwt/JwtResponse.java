package com.softkit.controller.jwt;


import com.softkit.controller.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private Integer id;
    private String token;

    private String type = "Bearer";
    private String refreshToken;
    private String username;
    private String email;
    private List<Role> roles;

    public JwtResponse(String token, String refreshToken, Integer id, String username, String email, List<Role> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
