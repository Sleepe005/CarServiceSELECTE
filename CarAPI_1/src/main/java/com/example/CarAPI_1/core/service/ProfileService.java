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

    public void saveOrUpdateProfile(UserEntity user, ProfileEntity newProfileModel){
        if(user.getProfile() != null){
//            Обновляем существующий профиль
            ProfileEntity existing = user.getProfile();
            existing.setName(newProfileModel.getName());
            existing.setBudgetMax(newProfileModel.getBudgetMax());
            existing.setUsagePurpose(newProfileModel.getUsagePurpose());
            existing.setWeightPrice(newProfileModel.getWeightPrice());
            existing.setWeightSafety(newProfileModel.getWeightSafety());
            existing.setWeightReliability(newProfileModel.getWeightReliability());
            existing.setWeightEconomy(newProfileModel.getWeightEconomy());
            existing.setWeightComfort(newProfileModel.getWeightComfort());
            existing.setWeightCapacity(newProfileModel.getWeightCapacity());
            existing.setWeightDynamics(newProfileModel.getWeightDynamics());
            existing.setWeightAppearance(newProfileModel.getWeightAppearance());
            existing.setWeightServiceCost(newProfileModel.getWeightServiceCost());

            profilePort.save(existing);
        }

//        Создаём новый профиль и связываем с пользователем
        user.setProfile(newProfileModel);
        newProfileModel.setUser(user);

        profilePort.save(newProfileModel);
    }

    public ProfileEntity getProfileByUserId(Long userId) {

        return profilePort.findByUserId(userId).orElseThrow();
    }

    public void deleteProfileByUserId(Long userId) {
        profilePort.findByUserId(userId)
                .ifPresent(profilePort::deleteProfile);
    }
}