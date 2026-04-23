package com.example.CarAPI_1.service;

import com.example.CarAPI_1.model.Profile;
import com.example.CarAPI_1.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public Profile saveProfile(Profile profile) {
        Optional<Profile> existingProfile = profileRepository.findByUserId(profile.getUserId());

        if (existingProfile.isPresent()) {
            Profile existing = existingProfile.get();
            existing.setName(profile.getName());
            existing.setBudget_max(profile.getBudget_max());
            existing.setUsage_purpose(profile.getUsage_purpose());
            existing.setWeight_price(profile.getWeight_price());
            existing.setWeight_safety(profile.getWeight_safety());
            existing.setWeight_reliability(profile.getWeight_reliability());
            existing.setWeight_economy(profile.getWeight_economy());
            existing.setWeight_comfort(profile.getWeight_comfort());
            existing.setWeight_capacity(profile.getWeight_capacity());
            existing.setWeight_dynamics(profile.getWeight_dynamics());
            existing.setWeight_appearance(profile.getWeight_appearance());
            existing.setWeight_service_cost(profile.getWeight_service_cost());
            return profileRepository.save(existing);
        }

        return profileRepository.save(profile);
    }

    public Optional<Profile> getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteProfile(Long userId) {
        profileRepository.findByUserId(userId)
                .ifPresent(profileRepository::delete);
    }
}