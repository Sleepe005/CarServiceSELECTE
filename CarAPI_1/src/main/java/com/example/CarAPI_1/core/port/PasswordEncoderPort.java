package com.example.CarAPI_1.core.port;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
    Boolean matches(String raw, String hash);
}
