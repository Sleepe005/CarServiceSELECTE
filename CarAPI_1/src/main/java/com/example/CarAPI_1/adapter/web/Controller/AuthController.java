package com.example.CarAPI_1.adapter.web.Controller;

import com.example.CarAPI_1.adapter.web.DTO.LoginRequest;
import com.example.CarAPI_1.adapter.web.DTO.RegisterRequest;
import com.example.CarAPI_1.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request){
        userService.registerUser(
                request.getEmail(),
                request.getRawPass(),
                request.getFullName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest request){
        return userService.authenticate(request.getEmail(), request.getPassword());
    }
}
