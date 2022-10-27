package com.softkit.dto.response;

import com.softkit.controller.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Integer id;

    private String username;

    private String email;

    private List<Role> roles;
}
