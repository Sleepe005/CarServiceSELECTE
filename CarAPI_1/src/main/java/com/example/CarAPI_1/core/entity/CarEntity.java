package com.example.CarAPI_1.core.entity;

import java.time.LocalDate;

public class CarEntity {
    private Long id;
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

    public CarEntity() {
    }

    public CarEntity(Long id, String brand, String model, String generation,
                     LocalDate yearFrom, LocalDate yearTo, Long price,
                     String bodyType, String fuelType, String transmission,
                     String drivetrain, Long powerHp, Long safetyRating,
                     Long reliabilityRating, Long economyRating, Long comfortRating,
                     Long capacityRating, Long dynamicsRating, Long appearanceRating,
                     Long featuresRating) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.generation = generation;
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
        this.price = price;
        this.bodyType = bodyType;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.drivetrain = drivetrain;
        this.powerHp = powerHp;
        this.safetyRating = safetyRating;
        this.reliabilityRating = reliabilityRating;
        this.economyRating = economyRating;
        this.comfortRating = comfortRating;
        this.capacityRating = capacityRating;
        this.dynamicsRating = dynamicsRating;
        this.appearanceRating = appearanceRating;
        this.featuresRating = featuresRating;
    }

    // Конструктор без ID (для создания новых объектов)
    public CarEntity(String brand, String model, String generation,
                     LocalDate yearFrom, LocalDate yearTo, Long price,
                     String bodyType, String fuelType, String transmission,
                     String drivetrain, Long powerHp, Long safetyRating,
                     Long reliabilityRating, Long economyRating, Long comfortRating,
                     Long capacityRating, Long dynamicsRating, Long appearanceRating,
                     Long featuresRating) {
        this(null, brand, model, generation, yearFrom, yearTo, price,
                bodyType, fuelType, transmission, drivetrain, powerHp,
                safetyRating, reliabilityRating, economyRating, comfortRating,
                capacityRating, dynamicsRating, appearanceRating, featuresRating);
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public LocalDate getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(LocalDate yearFrom) {
        this.yearFrom = yearFrom;
    }

    public LocalDate getYearTo() {
        return yearTo;
    }

    public void setYearTo(LocalDate yearTo) {
        this.yearTo = yearTo;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getDrivetrain() {
        return drivetrain;
    }

    public void setDrivetrain(String drivetrain) {
        this.drivetrain = drivetrain;
    }

    public Long getPowerHp() {
        return powerHp;
    }

    public void setPowerHp(Long powerHp) {
        this.powerHp = powerHp;
    }

    public Long getSafetyRating() {
        return safetyRating;
    }

    public void setSafetyRating(Long safetyRating) {
        this.safetyRating = safetyRating;
    }

    public Long getReliabilityRating() {
        return reliabilityRating;
    }

    public void setReliabilityRating(Long reliabilityRating) {
        this.reliabilityRating = reliabilityRating;
    }

    public Long getEconomyRating() {
        return economyRating;
    }

    public void setEconomyRating(Long economyRating) {
        this.economyRating = economyRating;
    }

    public Long getComfortRating() {
        return comfortRating;
    }

    public void setComfortRating(Long comfortRating) {
        this.comfortRating = comfortRating;
    }

    public Long getCapacityRating() {
        return capacityRating;
    }

    public void setCapacityRating(Long capacityRating) {
        this.capacityRating = capacityRating;
    }

    public Long getDynamicsRating() {
        return dynamicsRating;
    }

    public void setDynamicsRating(Long dynamicsRating) {
        this.dynamicsRating = dynamicsRating;
    }

    public Long getAppearanceRating() {
        return appearanceRating;
    }

    public void setAppearanceRating(Long appearanceRating) {
        this.appearanceRating = appearanceRating;
    }

    public Long getFeaturesRating() {
        return featuresRating;
    }

    public void setFeaturesRating(Long featuresRating) {
        this.featuresRating = featuresRating;
    }
}