package com.softkit.mapper;

import com.softkit.dto.UserDataDTO;
import com.softkit.dto.response.UserResponseDTO;
import com.softkit.controller.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapUserDataToUser(UserDataDTO e);

    UserResponseDTO mapUserToResponse(User e);
}
