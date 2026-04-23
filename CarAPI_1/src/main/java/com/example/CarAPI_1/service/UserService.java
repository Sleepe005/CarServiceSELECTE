package com.example.CarAPI_1.service;

import com.example.CarAPI_1.model.Profile;
import com.example.CarAPI_1.model.User;
import com.example.CarAPI_1.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ProfileService profileService;

    @Transactional
    public void registerUser(String email, String rawPassword, String fullName){
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("User already exists");
        }

        String hashPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email, hashPassword, fullName);
        userRepository.save(user);

        profileService.saveOrUpdateProfile(new Profile(user.getId(), user.getFullName()));
    }

    public String authenticate(String email, String rawPassword){
        User user = validatePassword(email, rawPassword);
        if(user == null){
            throw new RuntimeException("Incorrect login or password");
        }

        return jwtService.generateToken(user.getEmail(), user.getId(), "User");
    }

    public Boolean validatePassword(Long userId, String rawPassword){
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        String hashPassword = user.getPasswordHash();
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    public User validatePassword(String email, String rawPassword){
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        String hashPassword = user.getPasswordHash();

        if(passwordEncoder.matches(rawPassword, hashPassword)){return user;}
        return null;
    }
}
