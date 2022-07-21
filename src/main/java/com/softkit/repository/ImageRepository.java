package com.softkit.repository;

import com.softkit.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Query("SELECT img FROM Image img WHERE img.user.id=:id")
    Image getImage(Long id);
}
