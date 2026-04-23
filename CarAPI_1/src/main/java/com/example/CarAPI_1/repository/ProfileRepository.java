package com.example.CarAPI_1.repository;

import com.example.CarAPI_1.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

//    @Query("SELECT p FROM Profile p WHERE p.user_id = :userId")
    Optional<Profile> findByUserId(Long userId);

//    @Query("SELECT COUNT(p) > 0 FROM Profile p WHERE p.user_id = :userId")
    boolean existsByUserId(Long userId);
}