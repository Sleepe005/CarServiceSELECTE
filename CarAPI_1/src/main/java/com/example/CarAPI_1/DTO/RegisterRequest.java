package com.example.CarAPI_1.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String rawPass;
    private String fullName;
}
