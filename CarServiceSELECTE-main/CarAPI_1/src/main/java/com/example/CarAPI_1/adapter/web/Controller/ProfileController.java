package com.example.CarAPI_1.adapter.web.Controller;

import com.example.CarAPI_1.adapter.database.mapper.ProfileMapper;
import com.example.CarAPI_1.adapter.database.mapper.UserMapper;
import com.example.CarAPI_1.adapter.database.model.ProfileModel;
import com.example.CarAPI_1.adapter.database.model.UserModel;
import com.example.CarAPI_1.adapter.repository.ProfileRepository;
import com.example.CarAPI_1.adapter.repository.UserRepository;
import com.example.CarAPI_1.adapter.web.DTO.ProfileDTO;
import com.example.CarAPI_1.core.entity.ProfileEntity;
import com.example.CarAPI_1.core.service.ProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;

    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProfileDTO> getProfile(
            @Parameter(hidden = true)Authentication authentication){
        String email = authentication.getName();
        UserModel user = userRepository.findUserByEmail(email).get();
        ProfileModel profile = user.getProfileModel();
        return ResponseEntity.ok(new ProfileDTO(profile));
    }

    @PutMapping("/me")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> updateProfile(
            @Parameter(hidden = true)Authentication authentication, @RequestBody ProfileDTO newProfile){
        String email = authentication.getName();
        UserModel user = userRepository.findUserByEmail(email).get();
        ProfileModel profile = newProfile.toProfileModel(newProfile);
        profileService.saveOrUpdateProfile(userMapper.toDomain(user), profileMapper.toDomain(profile));
        return ResponseEntity.ok().build();
    }
}
