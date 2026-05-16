package com.example.CarAPI_1.core.service;

import com.example.CarAPI_1.core.entity.ProfileEntity;
import com.example.CarAPI_1.core.entity.UserEntity;
import com.example.CarAPI_1.core.port.PasswordEncoderPort;
import com.example.CarAPI_1.core.port.TokenPort;
import com.example.CarAPI_1.core.port.UserPort;

public class UserService {
    private final PasswordEncoderPort passwordEncoderPort;
    private final UserPort userPort;
    private final TokenPort tokenPort;
    private final ProfileService profileService;

    public UserService(PasswordEncoderPort passwordEncoderPort, UserPort userPort, TokenPort tokenPort, ProfileService profileService) {
        this.passwordEncoderPort = passwordEncoderPort;
        this.userPort = userPort;
        this.tokenPort = tokenPort;
        this.profileService = profileService;
    }

    public void registerUser(String email, String rawPassword, String fullName){
        if(userPort.existsByEmail(email)){
            throw new RuntimeException("UserModel already exists");
        }

        String hashPassword = passwordEncoderPort.encode(rawPassword);
        UserEntity user = new UserEntity(email, hashPassword, fullName);

        userPort.save(user);

        profileService.saveOrUpdateProfile(user, new ProfileEntity(fullName));
    }

    public String authenticate(String email, String rawPassword){
        UserEntity user = validatePassword(email, rawPassword);
        if(user == null){
            throw new RuntimeException("Incorrect login or password");
        }

        return tokenPort.generateToken(user.getEmail(), user.getId(), "UserModel");
    }

    public Boolean validatePassword(Long userId, String rawPassword){
        UserEntity user = userPort.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("UserEntity does not exist"));
        String hashPassword = user.getPasswordHash();
        return passwordEncoderPort.matches(rawPassword, hashPassword);
    }

    public UserEntity validatePassword(String email, String rawPassword){
        UserEntity user = userPort.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("UserEntity does not exist"));
        String hashPassword = user.getPasswordHash();

        if(passwordEncoderPort.matches(rawPassword, hashPassword)) {return user;}
        return null;
    }
}
