package com.example.CarAPI_1.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private  Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "budget_max", nullable = true)
    private Long budget_max;

    @Column(name = "usage_purpose", nullable = false)
    private Integer usage_purpose = 1;

    @Column(name = "weight_price", nullable = false)
    private Integer weight_price = 5;

    @Column(name = "weight_safety", nullable = false)
    private Integer weight_safety = 5;

    @Column(name = "weight_reliability", nullable = false)
    private Integer weight_reliability = 5;

    @Column(name = "weight_economy", nullable = false)
    private Integer weight_economy = 5;

    @Column(name = "weight_comfort", nullable = false)
    private Integer weight_comfort = 5;

    @Column(name = "weight_capacity", nullable = false)
    private Integer weight_capacity = 5;

    @Column(name = "weight_dynamics", nullable = false)
    private Integer weight_dynamics = 5;

    @Column(name = "weight_appearance", nullable = false)
    private Integer weight_appearance = 5;

    @Column(name = "weight_service_cost", nullable = false)
    private Integer weight_service_cost = 5;

    public Profile(){}

    public Profile(Long user_id, String name){
        this.userId = user_id;
        this.name = name;
    }
}
