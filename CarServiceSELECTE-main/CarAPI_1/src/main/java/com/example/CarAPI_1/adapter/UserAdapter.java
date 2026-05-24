package com.example.CarAPI_1.adapter;

import com.example.CarAPI_1.adapter.database.mapper.UserMapper;
import com.example.CarAPI_1.adapter.database.model.UserModel;
import com.example.CarAPI_1.adapter.repository.UserRepository;
import com.example.CarAPI_1.core.entity.UserEntity;
import com.example.CarAPI_1.core.port.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<UserEntity> findUserById(Long id){
        UserModel userModel = userRepository.findUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found by id: " + id));
        UserEntity user = userMapper.toDomain(userModel);
        return Optional.of(user);
    }
    public Optional<UserEntity> findUserByEmail(String email){
        UserModel userModel = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found by email: " + email));
        UserEntity user = userMapper.toDomain(userModel);
        return Optional.of(user);
    }
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public void save (UserEntity user){
        UserModel userModel = userMapper.toModel(user);
        userRepository.save(userModel);
    }
}
