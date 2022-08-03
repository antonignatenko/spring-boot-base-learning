package com.softkit.service;

import com.softkit.exception.CustomException;
import com.softkit.model.Role;
import com.softkit.model.User;
import com.softkit.repository.UserRepository;
import com.softkit.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String signin(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            User currentUserFromDb = userRepository.findByUsername(username);
         /*   if (!currentUserFromDb.isActivated()) {
                throw new CustomException("Users email wasn't yet activated, please activate your account", HttpStatus.BAD_REQUEST);
            }

          */
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signup(User user) {
        if (!userRepository.existsByUsername(user.getUsername())
                && !userRepository.existsByEmail(user.getEmail().toLowerCase())) {
            if (user.getRoles() == null) {
                user.setRoles(List.of(Role.ROLE_CLIENT));
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUUID(getUUidForUser());
            user.setRegistrationDate(LocalDateTime.now());
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new CustomException("Username or user's email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User whoami(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    //  method must delete user, by username, throw appropriate exception if user doesn't exist
    public void delete(String username) {
        try {
            User userFromDB = userRepository.findByUsername(username);
            if (userFromDB != null) {
                userRepository.delete(userFromDB);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User with such username:  was not found", e);
        }
    }

    //  method must search user, by username, throw appropriate exception is user doesn't exists
    public User search(String username) {
        User userFromDB = userRepository.findByUsername(username);
        if (userFromDB != null) {
            return userFromDB;
        } else {
            throw new UsernameNotFoundException("user with such username was not found");
        }
    }

    //  method must create a new access token, similar to login
    public String refresh(String username) {
        User userFromDB = userRepository.findByUsername(username);
        if (userFromDB == null) {
            throw new UsernameNotFoundException("user with such username was not found");
        }
        return jwtTokenProvider.createToken(username, userFromDB.getRoles());
    }

    public String getUUidForUser() {
        return UUID.randomUUID().toString();
    }

    public void activateUser(int id) {
        User userFromDB = userRepository.findById(id).get();
        if (userFromDB != null) {
            userFromDB.setActivated(true);
            userRepository.updateActivationStatus(userFromDB.getId(), userFromDB.isActivated());
        } else {
            throw new UsernameNotFoundException("Sorry such user wasnt found ");
        }
    }

    public void updateUsersInfo(String userName,String firstName,String lastName) {
        User userFromDB = userRepository.findByUsername(userName);
        if (userFromDB != null) {
            userFromDB.setFirstName(firstName);
            userFromDB.setLastName(lastName);
            userRepository.save(userFromDB);
        }
        else {
            throw new UsernameNotFoundException("user with such username have not been found");
        }
    }
}
