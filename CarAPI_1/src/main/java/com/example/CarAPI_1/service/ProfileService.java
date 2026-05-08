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
    public Profile saveOrUpdateProfile(User user, Profile newProfile){
        if(user.getProfile() != null){
//            Обновляем существующий профиль
            Profile existing = user.getProfile();
            existing.setName(newProfile.getName());
            existing.setBudget_max(newProfile.getBudget_max());
            existing.setUsage_purpose(newProfile.getUsage_purpose());
            existing.setWeight_price(newProfile.getWeight_price());
            existing.setWeight_safety(newProfile.getWeight_safety());
            existing.setWeight_reliability(newProfile.getWeight_reliability());
            existing.setWeight_economy(newProfile.getWeight_economy());
            existing.setWeight_comfort(newProfile.getWeight_comfort());
            existing.setWeight_capacity(newProfile.getWeight_capacity());
            existing.setWeight_dynamics(newProfile.getWeight_dynamics());
            existing.setWeight_appearance(newProfile.getWeight_appearance());
            existing.setWeight_service_cost(newProfile.getWeight_service_cost());

            return profileRepository.save(existing);
        }

//        Создаём новый профиль и связываем с пользователем
        user.setProfile(newProfile);
        newProfile.setUser(user);

        return profileRepository.save(newProfile);
    }

    public Profile getProfileByUserId(Long userId) {

        return profileRepository.findByUserId(userId).orElseThrow();
    }

    @Transactional
    public void deleteProfile(Long userId) {
        profileRepository.findByUserId(userId)
                .ifPresent(profileRepository::delete);
    }
}