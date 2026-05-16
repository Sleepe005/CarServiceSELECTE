package com.example.CarAPI_1.adapter;

import com.example.CarAPI_1.core.port.*;
import com.example.CarAPI_1.core.service.CarService;
import com.example.CarAPI_1.core.service.ProfileService;
import com.example.CarAPI_1.core.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public CarService carService(CarPort carPort){
        return new CarService(carPort);
    }

    @Bean
    public ProfileService profileService(ProfilePort profilePort, UserPort userPort){
        return new ProfileService(profilePort, userPort);
    }

    @Bean
    public UserService userService(PasswordEncoderPort passwordEncoderPort, UserPort userPort, TokenPort tokenPort, ProfileService profileService) {
        return new UserService(passwordEncoderPort, userPort, tokenPort, profileService);
    }
}
