package com.example.CarAPI_1.core.port;

import com.example.CarAPI_1.core.entity.ProfileEntity;

import java.util.Optional;

public interface ProfilePort {
    Optional<ProfileEntity> findByUserId(Long id);
    boolean existByUserId(Long id);
    void save(ProfileEntity profile);
    void deleteProfile(ProfileEntity profile);
}
