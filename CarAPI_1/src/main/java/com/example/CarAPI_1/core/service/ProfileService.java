package com.example.CarAPI_1.core.service;

import com.example.CarAPI_1.core.entity.ProfileEntity;
import com.example.CarAPI_1.core.entity.UserEntity;
import com.example.CarAPI_1.core.port.ProfilePort;
import com.example.CarAPI_1.core.port.UserPort;

public class ProfileService {

    private final ProfilePort profilePort;
    private final UserPort userPort;

    public ProfileService(ProfilePort profilePort, UserPort userPort) {
        this.profilePort = profilePort;
        this.userPort = userPort;
    }

    public void saveOrUpdateProfile(UserEntity user, ProfileEntity newProfileData) {
        if (profilePort.existByUserId(user.getId())) {
            // UPDATE — загружаем существующий и обновляем
            ProfileEntity existing = profilePort.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            existing.setName(newProfileData.getName());
            existing.setBudgetMax(newProfileData.getBudgetMax());
            existing.setUsagePurpose(newProfileData.getUsagePurpose());
            existing.setWeightPrice(newProfileData.getWeightPrice());
            existing.setWeightSafety(newProfileData.getWeightSafety());
            existing.setWeightReliability(newProfileData.getWeightReliability());
            existing.setWeightEconomy(newProfileData.getWeightEconomy());
            existing.setWeightComfort(newProfileData.getWeightComfort());
            existing.setWeightCapacity(newProfileData.getWeightCapacity());
            existing.setWeightDynamics(newProfileData.getWeightDynamics());
            existing.setWeightAppearance(newProfileData.getWeightAppearance());
            existing.setWeightServiceCost(newProfileData.getWeightServiceCost());

            profilePort.save(existing);
        } else {
            // INSERT — новый профиль
            newProfileData.setUser(user);
            profilePort.save(newProfileData);
        }
    }

    public ProfileEntity getProfileByUserId(Long userId) {
        return profilePort.findByUserId(userId).orElseThrow();
    }

    public void deleteProfileByUserId(Long userId) {
        profilePort.findByUserId(userId)
                .ifPresent(profilePort::deleteProfile);
    }
}