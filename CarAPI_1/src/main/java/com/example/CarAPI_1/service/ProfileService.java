package com.example.CarAPI_1.service;

import com.example.CarAPI_1.model.Profile;
import com.example.CarAPI_1.model.User;
import com.example.CarAPI_1.repository.ProfileRepository;
import com.example.CarAPI_1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional
    public Profile saveProfile(Long userId, Profile profileData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Optional<Profile> existingProfile = profileRepository.findByUserId(userId);

        Profile profile;
        if (existingProfile.isPresent()) {
            profile = existingProfile.get();
            updateProfileFields(profile, profileData);
        } else {
            profile = profileData;
            profile.setUser(user);
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

    private void updateProfileFields(Profile target, Profile source) {
        if (source.getUsagePurpose() != null) target.setUsagePurpose(source.getUsagePurpose());
        if (source.getBudget() != null) target.setBudget(source.getBudget());
        if (source.getBodyType() != null) target.setBodyType(source.getBodyType());
        if (source.getFuelType() != null) target.setFuelType(source.getFuelType());
        if (source.getTransmission() != null) target.setTransmission(source.getTransmission());
        if (source.getDriveType() != null) target.setDriveType(source.getDriveType());
        if (source.getMinPower() != null) target.setMinPower(source.getMinPower());
        if (source.getMaxFuelConsumption() != null) target.setMaxFuelConsumption(source.getMaxFuelConsumption());
        if (source.getPricePriority() != null) target.setPricePriority(source.getPricePriority());
        if (source.getSafetyPriority() != null) target.setSafetyPriority(source.getSafetyPriority());
        if (source.getReliabilityPriority() != null) target.setReliabilityPriority(source.getReliabilityPriority());
        if (source.getEconomyPriority() != null) target.setEconomyPriority(source.getEconomyPriority());
        if (source.getComfortPriority() != null) target.setComfortPriority(source.getComfortPriority());
        if (source.getCapacityPriority() != null) target.setCapacityPriority(source.getCapacityPriority());
        if (source.getDynamicsPriority() != null) target.setDynamicsPriority(source.getDynamicsPriority());
        if (source.getAppearancePriority() != null) target.setAppearancePriority(source.getAppearancePriority());
    }
}