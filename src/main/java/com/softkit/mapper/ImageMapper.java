package com.softkit.mapper;

import com.softkit.dto.ImageResponseDTO;
import com.softkit.controller.model.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageResponseDTO mapImageToResponse(Image image);
}
