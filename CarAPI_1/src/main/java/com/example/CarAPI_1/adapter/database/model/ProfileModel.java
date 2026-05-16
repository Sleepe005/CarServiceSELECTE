package com.example.CarAPI_1.adapter.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "user_id", nullable = false)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "budget_max", nullable = true)
    private Integer budgetMax = 0;

    @Column(name = "usage_purpose", nullable = false)
    private Integer usagePurpose = 1;

    @Column(name = "weight_price", nullable = false)
    private Integer weightPrice = 5;

    @Column(name = "weight_safety", nullable = false)
    private Integer weightSafety = 5;

    @Column(name = "weight_reliability", nullable = false)
    private Integer weightReliability = 5;

    @Column(name = "weight_economy", nullable = false)
    private Integer weightEconomy = 5;

    @Column(name = "weight_comfort", nullable = false)
    private Integer weightComfort = 5;

    @Column(name = "weight_capacity", nullable = false)
    private Integer weightCapacity = 5;

    @Column(name = "weight_dynamics", nullable = false)
    private Integer weightDynamics = 5;

    @Column(name = "weight_appearance", nullable = false)
    private Integer weightAppearance = 5;

    @Column(name = "weight_service_cost", nullable = false)
    private Integer weightServiceCost = 5;

    public ProfileModel(){}

    public ProfileModel(String name){
        this.name = name;
    }
}
