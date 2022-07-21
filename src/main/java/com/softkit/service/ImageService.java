package com.softkit.service;

import com.softkit.dto.ImageDTO;
import com.softkit.dto.ImageResponseDTO;
import com.softkit.dto.UserDataDTO;
import com.softkit.mapper.ImageMapper;
import com.softkit.model.Image;
import com.softkit.model.User;
import com.softkit.repository.ImageRepository;
import com.softkit.repository.UserRepository;
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
public class ImageService {

    private final UserRepository userRepository;

    private final ImageMapper imageMapper;

    private final ImageRepository imageRepository;

    @Value("${app.nfs.path:/Users/antonignatenko/Downloads/fileUploads}")
    private String localMachinePath;


    @SneakyThrows
    public ImageResponseDTO postImage(UserDataDTO userDataDTO, MultipartFile file, String title) {
        User userFromDB = userRepository.findByUsername(userDataDTO.getUsername());
        if (userFromDB == null) {
            throw new UsernameNotFoundException("User with such name wasn't found");
        }

        String path = localMachinePath
                + "/" + UUID.randomUUID()
                + "-" + file.getOriginalFilename();

        Image image = Image.builder()
                .title(title)
                .user(userFromDB)
                .path(path)
                .build();

        File storedImage = new File(path);
        storedImage.createNewFile();

        file.transferTo(storedImage);
        userFromDB.getImages().add(image);

        imageRepository.save(image);
        userRepository.save(userFromDB);

        return imageMapper.mapImageToResponse(image);
    }

    public String getImage(Long id) {
        Image imageFromDB = imageRepository.getImage(id);
        if (imageFromDB != null) {
            return imageFromDB.getPath();
        } else {
            throw new UsernameNotFoundException("sorry such image wasn't found");
        }
    }
}

