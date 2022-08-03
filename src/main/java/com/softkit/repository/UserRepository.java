package com.softkit.repository;

import com.softkit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    @Modifying()
    @Transactional
    @Query("UPDATE users u set u.isActivated =:status WHERE u.id =:id")
    void updateActivationStatus(@Param("id") Integer id, @Param("status") boolean status);

    @Override
    Optional<User> findById(Integer id);

    boolean existsByEmail(String email);


    @Query("FROM users u LEFT JOIN FETCH u.images where u.username= :username")
    User findByUsername(String username);

    @Query("FROM users u LEFT JOIN FETCH u.images where u.firstName= :firstName")
    User findByFirstName(String firstName);

}
