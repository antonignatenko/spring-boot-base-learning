package com.softkit.configuration;

import com.softkit.repository.ImageRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    private final ImageRepository imageRepository;

    public ResourceConfig(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "";
        registry.addResourceHandler("/content/**")
                .addResourceLocations(path);
    }
}
