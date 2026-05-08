package com.example.CarAPI_1.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Cars")
public class Cars {

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
    private java.time.LocalDate year_from;

    @Column(name="year_to", nullable = false)
    private java.time.LocalDate year_to;

    @Column(name="price", nullable = false)
    private Long price;

    @Column(name="body_type", nullable = false)
    private String body_type;

    @Column(name="fuel_type", nullable = false)
    private String fuel_type;

    @Column(name="transmission", nullable = false)
    private String transmission;

    @Column(name="drivetrain", nullable = false)
    private String drivetrain;

    @Column(name="power_hp", nullable = false)
    private Long power_hp;




    //пользовательские критерии для electre
    @Column(name="safety_rating", nullable = false)
    private Long safety_rating;

    @Column(name="reliability_rating", nullable = false)
    private Long reliability_rating;

    @Column(name="economy_rating", nullable = false)
    private Long economy_rating;

    @Column(name="comfort_rating", nullable = false)
    private Long comfort_rating;

    @Column(name="capacity_rating", nullable = false)
    private Long capacity_rating;

    @Column(name="dynamics_rating", nullable = false)
    private Long dynamics_rating;

    @Column(name="appearance_rating", nullable = false)
    private Long appearance_rating;

    @Column(name="features_rating", nullable = false)
    private Long features_rating;


}