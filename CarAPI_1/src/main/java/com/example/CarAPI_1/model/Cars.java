package com.example.CarAPI_1.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Cars {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

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

    @Column(name="safety_rating", nullable = false)
    private Long safety_rating;

    @Column(name="reliability_rating", nullable = false)
    private Long reliability_rating;

    @Column(name="fuel_consumption", nullable = false)
    private Long fuel_consumption;

    @Column(name="maintenance_cost", nullable = false)
    private Long maintenance_cost;

    @Column(name="comfort_rating", nullable = false)
    private Long comfort_rating;

    @Column(name="capacity_passengers", nullable = false)
    private Long capacity_passengers;

    @Column(name="capacity_baggage", nullable = false)
    private Long capacity_baggage;

    @Column(name="acceleration_0_100", nullable = false)
    private Long acceleration_0_100;

    @Column(name="appearance_rating", nullable = false)
    private Long appearance_rating;

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

    @Column(name="additional_options", nullable = false)
    private String additional_options;

    @Column(name="last_updated", nullable = false)
    private java.time.LocalDate last_updated;

    public Cars(String brand, String model, String generation,
                LocalDate year_from, LocalDate year_to, Long price, Long safety_rating,
                Long reliability_rating, Long fuel_consumption, Long maintenance_cost,
                Long comfort_rating, Long capacity_passengers, Long capacity_baggage, Long acceleration_0_100,
                Long appearance_rating, String body_type, String fuel_type, String transmission,
                String drivetrain, Long power_hp, String additional_options, LocalDate last_updated) {
        this.brand = brand;
        this.model = model;
        this.generation = generation;
        this.year_from = year_from;
        this.year_to = year_to;
        this.price = price;
        this.safety_rating = safety_rating;
        this.reliability_rating = reliability_rating;
        this.fuel_consumption = fuel_consumption;
        this.maintenance_cost = maintenance_cost;
        this.comfort_rating = comfort_rating;
        this.capacity_passengers = capacity_passengers;
        this.capacity_baggage = capacity_baggage;
        this.acceleration_0_100 = acceleration_0_100;
        this.appearance_rating = appearance_rating;
        this.body_type = body_type;
        this.fuel_type = fuel_type;
        this.transmission = transmission;
        this.drivetrain = drivetrain;
        this.power_hp = power_hp;
        this.additional_options = additional_options;
        this.last_updated = last_updated;
    }

}