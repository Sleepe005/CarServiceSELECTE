package com.example.CarAPI_1.core.port;

import com.example.CarAPI_1.core.entity.UserEntity;

import java.util.Optional;

public interface UserPort {
    Optional<UserEntity> findUserById(Long id);
    Optional<UserEntity> findUserByEmail(String email);
    Boolean existsByEmail(String email);
    void save (UserEntity user);
}
