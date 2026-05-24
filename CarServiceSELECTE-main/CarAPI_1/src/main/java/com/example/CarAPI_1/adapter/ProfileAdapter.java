package com.example.CarAPI_1.adapter;

import com.example.CarAPI_1.adapter.database.mapper.ProfileMapper;
import com.example.CarAPI_1.adapter.database.model.ProfileModel;
import com.example.CarAPI_1.adapter.repository.ProfileRepository;
import com.example.CarAPI_1.core.entity.ProfileEntity;
import com.example.CarAPI_1.core.port.ProfilePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfileAdapter implements ProfilePort {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public Optional<ProfileEntity> findByUserId(Long id) {
        ProfileModel profileModel = profileRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + id));
        ProfileEntity profile = profileMapper.toDomain(profileModel);
        return Optional.of(profile);
    }
    public boolean existByUserId(Long id){
        return profileRepository.existsByUserId(id);
    }
    public void save(ProfileEntity profile){
        ProfileModel profileModel = profileMapper.toModel(profile);
        profileRepository.save(profileModel);
    }
    public void deleteProfile(ProfileEntity profile){
        ProfileModel profileModel = profileMapper.toModel(profile);
        profileRepository.delete(profileModel);
    }
}
