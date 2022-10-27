package com.softkit.controller;

import com.softkit.dto.TokenRefreshRequest;
import com.softkit.dto.UserDataDTO;
import com.softkit.dto.response.TokenRefreshResponse;
import com.softkit.dto.response.UserResponseDTO;
import com.softkit.exception.TokenRefreshException;
import com.softkit.mapper.UserMapper;
import com.softkit.controller.model.ConfirmationToken;
import com.softkit.controller.model.RefreshToken;
import com.softkit.controller.model.User;
import com.softkit.security.JwtTokenProvider;
import com.softkit.service.ConfirmationTokenService;
import com.softkit.service.impl.RefreshTokenService;
import com.softkit.service.impl.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final ConfirmationTokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<?> login(
            @ApiParam("Username") @RequestParam String username,
            @ApiParam("Password") @RequestParam String password) {
      return userService.signin(username, password);
    }

    @PostMapping("/signup")
    @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use")})
    public String signup(@ApiParam("Signup User") @Valid @RequestBody UserDataDTO user) {
        return userService.signup(userMapper.mapUserDataToUser(user));
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserResponseDTO whoami(HttpServletRequest req) {
        return userMapper.mapUserToResponse(userService.whoami(req));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = tokenProvider.getUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "some data is invalid")
    public void deleteByUsername(@RequestParam("username") String username) {
        userService.delete(username);
    }

    @GetMapping("/search")
    public UserResponseDTO searchUser(@RequestParam String username) {
        return userMapper.mapUserToResponse(userService.search(username));
    }

    @PostMapping("/update/firstname")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<User> updateFirstname(@RequestParam(value = "name") String firstName) {
        String usersName = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.updateFirstname(usersName, firstName));
    }

    @PostMapping("/update/lastname")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<User> updateLastname(@RequestParam(value = "name") String firstName) {
        String usersName = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.updateLastname(usersName, firstName));
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token =
                tokenService.findByConfirmationToken(confirmationToken);
        if (token != null) {
            User user = userService.findByEmail(token.getUserEntity().getEmail());
            user.setActivated(true);
            userService.save(user);
        } else {
            throw new RuntimeException("couldn't find a user by such token");
        }
        return new ResponseEntity<>("You have successfully confirmed your account", HttpStatus.CREATED);
    }
}
