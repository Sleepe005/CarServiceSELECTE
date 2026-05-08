package com.example.CarAPI_1.controller;

import com.example.CarAPI_1.DTO.RegisterRequest;
import com.example.CarAPI_1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

//    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request){
        userService.registerUser(
                request.getEmail(),
                request.getRawPass(),
                request.getFullName());
        return ResponseEntity.ok().build();
    }
}
