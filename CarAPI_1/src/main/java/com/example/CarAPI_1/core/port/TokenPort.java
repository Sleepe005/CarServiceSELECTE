// core/port/out/TokenPort.java
package com.example.CarAPI_1.core.port;

public interface TokenPort {
    String generateToken(String email, Long userId, String role);
    String extractEmail(String token);
    Long extractUserId(String token);
    String extractRole(String token);
    boolean validateToken(String token, String email);
}