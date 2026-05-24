package com.example.CarAPI_1.adapter.web.DTO;

import com.example.CarAPI_1.adapter.database.model.CarModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CarResponseDTO {
    private String brand;
    private String model;
    private String generation;
    private LocalDate yearFrom;
    private LocalDate yearTo;
    private Long price;
    private String bodyType;
    private String fuelType;
    private String transmission;
    private String drivetrain;
    private Long powerHp;
    private Long safetyRating;
    private Long reliabilityRating;
    private Long economyRating;
    private Long comfortRating;
    private Long capacityRating;
    private Long dynamicsRating;
    private Long appearanceRating;
    private Long featuresRating;

    public CarResponseDTO(CarModel carModel){
        this.brand = carModel.getBrand();
        this.model = carModel.getModel();
        this.generation = carModel.getGeneration();
        this.yearFrom = carModel.getYearFrom();
        this.yearTo = carModel.getYearTo();
        this.price = carModel.getPrice();
        this.bodyType = carModel.getBodyType();
        this.fuelType = carModel.getFuelType();
        this.transmission = carModel.getTransmission();
        this.drivetrain = carModel.getDrivetrain();
        this.powerHp = carModel.getPowerHp();

        this.safetyRating = carModel.getSafetyRating();
        this.reliabilityRating = carModel.getReliabilityRating();
        this.economyRating = carModel.getEconomyRating();
        this.comfortRating = carModel.getComfortRating();
        this.capacityRating = carModel.getCapacityRating();
        this.dynamicsRating = carModel.getDynamicsRating();
        this.appearanceRating = carModel.getAppearanceRating();
        this.featuresRating = carModel.getFeaturesRating();
    }
}
