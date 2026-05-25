package com.example.CarAPI_1.adapter.web.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarImportDTO {
    private String brand;
    private String model;
    private String generation;

    @JsonProperty("year_from")
    private Integer yearFrom;

    @JsonProperty("year_to")
    private Integer yearTo;

    @JsonProperty("body_type")
    private String bodyType;

    @JsonProperty("fuel_type")
    private String fuelType;

    private String transmission;
    private String drivetrain;

    @JsonProperty("power_hp")
    private Integer powerHp;

    @JsonProperty("fuel_consumption")
    private Double fuelConsumption;

    private RatingsDto ratings;

    @Data
    public static class RatingsDto {
        private Integer safety;
        private Integer reliability;
        private Integer economy;
        private Integer comfort;
        private Integer capacity;
        private Integer dynamics;
        private Integer appearance;
        private Integer features;
    }
}

