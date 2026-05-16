package com.example.CarAPI_1.adapter.repository;

import com.example.CarAPI_1.adapter.database.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findUserById(Long id);
    Optional<UserModel> findUserByEmail(String email);
    Boolean existsByEmail(String email);
}
