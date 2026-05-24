package com.example.CarAPI_1.adapter.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CarModel")
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    //@Setter(AccessLevel.PROTECTED)
    private Long id;

    //минимальные тех критерии для вывода
    @Column(name="brand", nullable = false)
    private String brand;

    @Column(name="model", nullable = false)
    private String model;

    @Column(name="generation", nullable = false)
    private String generation;

    @Column(name="year_from", nullable = false)
    private java.time.LocalDate yearFrom;

    @Column(name="year_to", nullable = false)
    private java.time.LocalDate yearTo;

    @Column(name="price", nullable = false)
    private Long price;

    @Column(name="body_type", nullable = false)
    private String bodyType;

    @Column(name="fuel_type", nullable = false)
    private String fuelType;

    @Column(name="transmission", nullable = false)
    private String transmission;

    @Column(name="drivetrain", nullable = false)
    private String drivetrain;

    @Column(name="power_hp", nullable = false)
    private Long powerHp;




    //пользовательские критерии для electre
    @Column(name="safety_rating", nullable = false)
    private Long safetyRating;

    @Column(name="reliability_rating", nullable = false)
    private Long reliabilityRating;

    @Column(name="economy_rating", nullable = false)
    private Long economyRating;

    @Column(name="comfort_rating", nullable = false)
    private Long comfortRating;

    @Column(name="capacity_rating", nullable = false)
    private Long capacityRating;

    @Column(name="dynamics_rating", nullable = false)
    private Long dynamicsRating;

    @Column(name="appearance_rating", nullable = false)
    private Long appearanceRating;

    @Column(name="features_rating", nullable = false)
    private Long featuresRating;
}