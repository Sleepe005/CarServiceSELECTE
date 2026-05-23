// adapter/persistence/mapper/UserMapper.java
package com.example.CarAPI_1.adapter.database.mapper;

import com.example.CarAPI_1.adapter.database.model.ProfileModel;
import com.example.CarAPI_1.core.entity.ProfileEntity;
import com.example.CarAPI_1.core.entity.UserEntity;
import com.example.CarAPI_1.adapter.database.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // UserEntity (core) → UserModel (adapter)
    public UserModel toModel(UserEntity domain) {
        if (domain == null) return null;

        UserModel model = new UserModel();
        model.setId(domain.getId());
        model.setEmail(domain.getEmail());
        model.setPasswordHash(domain.getPasswordHash());
        model.setFullName(domain.getFullName());
        model.setCreatedAt(domain.getCreatedAt());

        if (domain.getProfile() != null) {
            ProfileEntity p = domain.getProfile();
            ProfileModel profileModel = new ProfileModel();
            profileModel.setId(p.getId());
            profileModel.setName(p.getName());
            profileModel.setBudgetMax(p.getBudgetMax());
            profileModel.setUsagePurpose(p.getUsagePurpose());
            profileModel.setWeightPrice(p.getWeightPrice());
            profileModel.setWeightSafety(p.getWeightSafety());
            profileModel.setWeightReliability(p.getWeightReliability());
            profileModel.setWeightEconomy(p.getWeightEconomy());
            profileModel.setWeightComfort(p.getWeightComfort());
            profileModel.setWeightCapacity(p.getWeightCapacity());
            profileModel.setWeightDynamics(p.getWeightDynamics());
            profileModel.setWeightAppearance(p.getWeightAppearance());
            profileModel.setWeightServiceCost(p.getWeightServiceCost());
            profileModel.setUser(model);
            model.setProfileModel(profileModel);
        }

        return model;
    }

    // UserModel (adapter) → UserEntity (core)
    public UserEntity toDomain(UserModel model) {
        if (model == null) {
            return null;
        }

        return new UserEntity(
                model.getId(),
                model.getEmail(),
                model.getPasswordHash(),
                model.getFullName(),
                model.getCreatedAt()
        );
    }

    public void updateModel(UserModel model, UserEntity domain) {
        if (domain == null || model == null) {
            return;
        }

        model.setEmail(domain.getEmail());
        model.setPasswordHash(domain.getPasswordHash());
        model.setFullName(domain.getFullName());
    }
}