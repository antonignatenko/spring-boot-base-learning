package com.softkit.controller;

import com.softkit.dto.EmailDTO;
import com.softkit.dto.UserDataDTO;
import com.softkit.dto.UserResponseDTO;
import com.softkit.mapper.UserMapper;
import com.softkit.service.EmailSenderService;
import com.softkit.service.FileUploadService;
import com.softkit.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {
    private final EmailSenderService senderService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Value("app.nfs.path")
    private String nfsPath;
    @Autowired
    private FileUploadService uploadService;

    @PostMapping("/signin")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String login(
            @ApiParam("Username") @RequestParam String username,
            @ApiParam("Password") @RequestParam String password) {
        return userService.signin(username, password);
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestParam String username) {
        return userService.refresh(username);
    }

    @PostMapping("/signup")
    @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username or user email are already in use")})
    public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
        return userService.signup(userMapper.mapUserDataToUser(user));
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class,
            authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserResponseDTO whoami(HttpServletRequest req) {
        return userMapper.mapUserToResponse(userService.whoami(req));
    }

    @DeleteMapping("/deleteByUsername")
    public void deleteByUsername (@RequestParam String username){
        userService.delete(username);
    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody EmailDTO emailDTO) {
        senderService.sendEmail(emailDTO.getToEmail(), emailDTO.getSubject(),emailDTO.getBody());
        // no security is used for now
    }
    @PostMapping("/search")
    public UserResponseDTO searchUserByUsername(@RequestParam String username){
       return userMapper.mapUserToResponse(userService.search(username));
    }

    @GetMapping("/activate/{id}")
    @ApiResponse(code = 200, message = "User has been activated succesfully")
    public void activateUser(@PathVariable int id){
        userService.activateUser(id);
    }

    @PostMapping("/upload")
    public void uploadPhoto(@RequestParam("file") MultipartFile file){
        uploadService.fileUpload(file);
    }
}
