package com.example.CarAPI_1.adapter.web.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String rawPass;
    private String fullName;
}
