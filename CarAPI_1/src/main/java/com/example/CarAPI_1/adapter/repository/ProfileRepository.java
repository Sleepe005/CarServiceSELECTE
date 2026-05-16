package com.example.CarAPI_1.adapter.repository;

import com.example.CarAPI_1.adapter.database.model.ProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileModel, Long> {

//    @Query("SELECT p FROM ProfileModel p WHERE p.user_id = :userId")
    Optional<ProfileModel> findByUserId(Long userId);

//    @Query("SELECT COUNT(p) > 0 FROM ProfileModel p WHERE p.user_id = :userId")
    boolean existsByUserId(Long userId);
}