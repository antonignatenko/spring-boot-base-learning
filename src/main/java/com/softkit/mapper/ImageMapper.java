package com.softkit.mapper;

import com.softkit.dto.ImageDTO;
import com.softkit.dto.ImageResponseDTO;
import com.softkit.model.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    Image mapImageDataToImage(ImageDTO imageDTO);

    ImageResponseDTO mapImageToResponse(Image image);
}
