package com.example.CarAPI_1.repository;

import com.example.CarAPI_1.model.Profile;
import com.example.CarAPI_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUser(User user);

    Optional<Profile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}