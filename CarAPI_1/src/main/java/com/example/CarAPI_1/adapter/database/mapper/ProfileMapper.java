// adapter/persistence/mapper/ProfileMapper.java
package com.example.CarAPI_1.adapter.database.mapper;

import com.example.CarAPI_1.core.entity.ProfileEntity;
import com.example.CarAPI_1.core.entity.UserEntity;
import com.example.CarAPI_1.adapter.database.model.ProfileModel;
import com.example.CarAPI_1.adapter.database.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileMapper {

    private final UserMapper userMapper;

    // Profile (core) → ProfileModel (adapter)
    public ProfileModel toModel(ProfileEntity domain) {
        if (domain == null) {
            return null;
        }

        ProfileModel model = new ProfileModel();
        model.setId(domain.getId());
        model.setName(domain.getName());
        model.setBudgetMax(domain.getBudgetMax());
        model.setUsagePurpose(domain.getUsagePurpose());
        model.setWeightPrice(domain.getWeightPrice());
        model.setWeightSafety(domain.getWeightSafety());
        model.setWeightReliability(domain.getWeightReliability());
        model.setWeightEconomy(domain.getWeightEconomy());
        model.setWeightComfort(domain.getWeightComfort());
        model.setWeightCapacity(domain.getWeightCapacity());
        model.setWeightDynamics(domain.getWeightDynamics());
        model.setWeightAppearance(domain.getWeightAppearance());
        model.setWeightServiceCost(domain.getWeightServiceCost());

        // Устанавливаем связь с User, если есть
        if (domain.getUser() != null) {
            UserModel userModel = userMapper.toModel(domain.getUser());
            model.setUser(userModel);
        }

        return model;
    }

    // ProfileModel (adapter) → Profile (core)
    public ProfileEntity toDomain(ProfileModel model) {
        if (model == null) {
            return null;
        }

        UserEntity user = null;
        if (model.getUser() != null) {
            user = userMapper.toDomain(model.getUser());
        }

        return new ProfileEntity(
                model.getId(),
                user,
                model.getName(),
                model.getBudgetMax(),
                model.getUsagePurpose(),
                model.getWeightPrice(),
                model.getWeightSafety(),
                model.getWeightReliability(),
                model.getWeightEconomy(),
                model.getWeightComfort(),
                model.getWeightCapacity(),
                model.getWeightDynamics(),
                model.getWeightAppearance(),
                model.getWeightServiceCost()
        );
    }

    // Обновление существующей ProfileModel из Profile (для update операций)
    public void updateModel(ProfileModel model, ProfileEntity domain) {
        if (domain == null || model == null) {
            return;
        }

        model.setName(domain.getName());
        model.setBudgetMax(domain.getBudgetMax());
        model.setUsagePurpose(domain.getUsagePurpose());
        model.setWeightPrice(domain.getWeightPrice());
        model.setWeightSafety(domain.getWeightSafety());
        model.setWeightReliability(domain.getWeightReliability());
        model.setWeightEconomy(domain.getWeightEconomy());
        model.setWeightComfort(domain.getWeightComfort());
        model.setWeightCapacity(domain.getWeightCapacity());
        model.setWeightDynamics(domain.getWeightDynamics());
        model.setWeightAppearance(domain.getWeightAppearance());
        model.setWeightServiceCost(domain.getWeightServiceCost());

        // Обновляем связь с User, если изменилась
        if (domain.getUser() != null && model.getUser() == null) {
            model.setUser(userMapper.toModel(domain.getUser()));
        } else if (domain.getUser() == null && model.getUser() != null) {
            model.setUser(null);
        } else if (domain.getUser() != null && model.getUser() != null) {
            userMapper.updateModel(model.getUser(), domain.getUser());
        }
    }
}