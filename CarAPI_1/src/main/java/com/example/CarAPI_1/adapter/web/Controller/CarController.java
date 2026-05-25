package com.example.CarAPI_1.adapter.web.Controller;

import com.example.CarAPI_1.adapter.database.mapper.ProfileMapper;
import com.example.CarAPI_1.adapter.database.model.CarModel;
import com.example.CarAPI_1.adapter.database.model.ProfileModel;
import com.example.CarAPI_1.adapter.database.model.UserModel;
import com.example.CarAPI_1.adapter.repository.CarRepository;
import com.example.CarAPI_1.adapter.repository.UserRepository;
import com.example.CarAPI_1.adapter.web.DTO.CarRankingDTO;
import com.example.CarAPI_1.adapter.web.DTO.CarResponseDTO;
import com.example.CarAPI_1.core.entity.CarEntity;
import com.example.CarAPI_1.core.entity.ProfileEntity;
import com.example.CarAPI_1.core.service.CarService;
import com.example.CarAPI_1.core.service.ElectreEngine;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CarController {

    private final CarRepository carRepository;
    private final CarService carService;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final ElectreEngine electreEngine;

    @GetMapping("allCars")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<CarResponseDTO>> getCarsForProfile(@Parameter(hidden = true) Authentication authentication){
        String email = authentication.getName();
        UserModel user = userRepository.findUserByEmail(email).get();
        ProfileModel profile = user.getProfileModel();

        List<CarModel> CarsModel = carRepository.findAll();
        List<CarResponseDTO> Cars = new ArrayList<>();
        for(CarModel carModel : CarsModel){
            Cars.add(new CarResponseDTO(carModel));
        }

        return ResponseEntity.ok(Cars);
    }

    @GetMapping("/rank")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<CarRankingDTO>> getRankedCars(@Parameter(hidden = true) Authentication authentication) {
        String email = authentication.getName();
        UserModel user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileModel profile = user.getProfileModel();
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        // Конвертируем ProfileModel → ProfileEntity
        ProfileEntity profileEntity = profileMapper.toDomain(profile);

        // Получаем все автомобили
        List<CarEntity> cars = carService.getAllCars();

        // Ранжируем
        List<ElectreEngine.CarRanking> rankings = electreEngine.rankCars(cars, profileEntity);

        // Конвертируем в DTO
        List<CarRankingDTO> result = rankings.stream()
                .map(CarRankingDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/getCar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<CarResponseDTO> getCar(@Parameter(hidden = true) Authentication authentication, Long carId){
        CarModel car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        return ResponseEntity.ok(new CarResponseDTO(car));
    }
}