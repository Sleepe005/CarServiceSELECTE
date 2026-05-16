// adapter/persistence/mapper/UserMapper.java
package com.example.CarAPI_1.adapter.database.mapper;

import com.example.CarAPI_1.core.entity.UserEntity;
import com.example.CarAPI_1.adapter.database.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // UserEntity (core) → UserModel (adapter)
    public UserModel toModel(UserEntity domain) {
        if (domain == null) {
            return null;
        }

        UserModel model = new UserModel();
        model.setId(domain.getId());
        model.setEmail(domain.getEmail());
        model.setPasswordHash(domain.getPasswordHash());
        model.setFullName(domain.getFullName());
        model.setCreatedAt(domain.getCreatedAt());

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