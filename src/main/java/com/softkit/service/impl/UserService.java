package com.softkit.service.impl;

import com.softkit.controller.jwt.JwtResponse;
import com.softkit.controller.model.RefreshToken;
import com.softkit.exception.CustomException;
import com.softkit.controller.model.ConfirmationToken;
import com.softkit.controller.model.Role;
import com.softkit.controller.model.User;
import com.softkit.repository.UserRepository;
import com.softkit.security.JwtTokenProvider;
import com.softkit.service.ConfirmationTokenService;
import com.softkit.service.EmailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService tokenService;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    @Value("${sender-email}")
    private String EMAIL_ADDRESS;
    @Value("${security.jwt.token.refresh-expire-length}")
    private int jwtExpirationMs;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public ResponseEntity<?> signin(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user != null && user.isActivated()) {
            try {
                Authentication authentication =
                        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException e) {
                throw new CustomException("Invalid username/password supplied or user wasn't activated",
                        HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new UsernameNotFoundException("such user wasn't found");
        }
        RefreshToken refreshToken = refreshTokenService.createAndAddRefreshTokenToDB(user.getId());
        String token = jwtTokenProvider.createToken(username, user.getRoles());

       return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken(), user.getId(),
                user.getUsername(), user.getEmail(), user.getRoles()));
    }

    public String signup(User user) {
        if (!userRepository.existsByUsernameIgnoreCase(user.getUsername()) &&
                !userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            LocalDateTime registrationTime = LocalDateTime.now();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActivated(false);
            user.setRegistrationDate(registrationTime);
            user.setRoles(List.of(Role.ROLE_CLIENT));
            user.setUUID(UUID.randomUUID().toString());
            userRepository.save(user);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            tokenService.saveToken(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom(EMAIL_ADDRESS);
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken());

            emailService.sendActivationEmail(mailMessage);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User whoami(HttpServletRequest req) {
        return userRepository.findByUsernameIgnoreCase(jwtTokenProvider.getUsername(jwtTokenProvider.extractJwtToken(req)));
    }

    //  method must delete user, by username, throw appropriate exception is user doesn't exists
    public void delete(String username) {
        try {
            userRepository.deleteUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            String usernameNotFound = String.format("username %s : wasn't found  ", username);
            throw new UsernameNotFoundException(usernameNotFound);
        }
    }

    //  method must search user, by username, throw appropriate exception is user doesn't exist
    public User search(String username) {
        User userFromDB = userRepository.findByUsernameIgnoreCase(username);
        if (userFromDB == null) {
            String usernameNotFound = String.format("username %s : wasn't found  ", username);
            throw new UsernameNotFoundException(usernameNotFound);
        } else {
            return userFromDB;
        }
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("such email wasn't found");
    }

    public void save(User user) {
        userRepository.save(user);
    }

    //  method must create a new access token, similar to login
    public String refresh(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    @Transactional
    public User updateFirstname(String userName, String firstname) {
        User user = userRepository.findByUsernameIgnoreCase(userName);
        if (user != null) {
            user.setFirstName(firstname);
            userRepository.save(user);
            return user;
        }
        throw new UsernameNotFoundException("such user name wasn't found");
    }

    @Transactional
    public User updateLastname(String userName, String lastname) {
        User user = userRepository.findByUsernameIgnoreCase(userName);
        if (user != null) {
            user.setLastName(lastname);
            userRepository.save(user);
            return user;
        }
        throw new UsernameNotFoundException("such user name wasn't found");
    }
}
