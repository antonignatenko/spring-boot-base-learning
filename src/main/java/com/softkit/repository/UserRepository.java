package com.softkit.repository;

import com.softkit.controller.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    User findByUsernameIgnoreCase(String username);
    void deleteUserByUsername(String name);

}
