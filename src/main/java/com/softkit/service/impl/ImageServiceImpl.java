package com.softkit.service.impl;

import com.softkit.dto.ImageResponseDTO;
import com.softkit.mapper.ImageMapper;
import com.softkit.controller.model.Image;
import com.softkit.controller.model.User;
import com.softkit.repository.ImageRepository;
import com.softkit.repository.UserRepository;
import com.softkit.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final UserRepository userRepository;

    private final ImageMapper imageMapper;

    private final ImageRepository imageRepository;

    @Value("${app.nfs.path}")
    private String localMachinePath;


    @SneakyThrows
    @Override
    public ImageResponseDTO postImage(String username, MultipartFile multipartFile, String title) {
        User userFromDB = userRepository.findByUsernameIgnoreCase(username);
        if (userFromDB == null) {
            throw new UsernameNotFoundException("User with such name wasn't found");
        }

        String path = localMachinePath
                + "/" + UUID.randomUUID()
                + "-" + multipartFile.getOriginalFilename();

        userFromDB.setPathToUsersPhoto(path);

        Image image = Image.builder()
                .title(title)
                .user(userFromDB)
                .path(path)
                .build();


        File storedImage = new File(path);
        storedImage.createNewFile();

        multipartFile.transferTo(storedImage);
        userFromDB.getImages().add(image);

        imageRepository.save(image);
        userRepository.save(userFromDB);

        return imageMapper.mapImageToResponse(image);
    }

    @Override
    public String getImage(Integer userId) {
        Image image = imageRepository.findByUserId(userId).orElseThrow(() ->
                new UsernameNotFoundException("sorry such image wasn't found"));
        return image.getPath();
    }
}

