package com.example.CarAPI_1.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private  Long user_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "budget_max", nullable = false)
    private Long budget_max;

    @Column(name = "usage_purpose", nullable = false)
    private Integer usage_purpose;

    @Column(name = "weight_price", nullable = false)
    private Integer weight_price;

    @Column(name = "weight_safety", nullable = false)
    private Integer weight_safety;

    @Column(name = "weight_reliability", nullable = false)
    private Integer weight_reliability;

    @Column(name = "weight_economy", nullable = false)
    private Integer weight_economy;

    @Column(name = "weight_comfort", nullable = false)
    private Integer weight_comfort;

    @Column(name = "weight_capacity", nullable = false)
    private Integer weight_capacity;

    @Column(name = "weight_dynamics", nullable = false)
    private Integer weight_dynamics;

    @Column(name = "weight_appearance", nullable = false)
    private Integer weight_appearance;

    @Column(name = "weight_service_cost", nullable = false)
    private Integer weight_service_cost;

    public Profile(){}

    public Profile(Long id, Long user_id, String name, Long budget_max,
                   Integer usage_purpose, Integer weight_price, Integer weight_safety,
                   Integer weight_reliability, Integer weight_economy, Integer weight_comfort,
                   Integer weight_capacity, Integer weight_dynamics, Integer weight_appearance,
                   Integer weight_service_cost){
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.budget_max = budget_max;
        this.usage_purpose = usage_purpose;
        this.weight_price = weight_price;
        this.weight_safety = weight_safety;
        this.weight_reliability = weight_reliability;
        this.weight_economy = weight_economy;
        this.weight_comfort = weight_comfort;
        this.weight_capacity = weight_capacity;
        this.weight_dynamics = weight_dynamics;
        this.weight_appearance = weight_appearance;
        this.weight_service_cost = weight_service_cost;
    }
}
