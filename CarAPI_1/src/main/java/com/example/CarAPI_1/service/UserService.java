package com.example.CarAPI_1.service;

import com.example.CarAPI_1.model.User;
import com.example.CarAPI_1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public void registerUser(String email, String rawPassword, String fullName){
        if(userRepository.existByEmail(email)){
            throw new RuntimeException("User already exists");
        }

        String hashPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email, hashPassword, fullName);
        userRepository.save(user);

//       TODO:: CreateProfile
    }

    public String authenticate(String email, String rawPassword){
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        if(!passwordEncoder.matches(rawPassword, user.getPasswordHash())){
            throw new RuntimeException("Incorrect login or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId(), "User");

        return token;
    }

    public Boolean validatePassword(Long userId, String rawPassword){
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        String hashPassword = user.getPasswordHash();
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    public Boolean validatePassword(String email, String rawPassword){
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        String hashPassword = user.getPasswordHash();
        return passwordEncoder.matches(rawPassword, hashPassword);
    }
}
