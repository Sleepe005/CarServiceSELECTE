package com.example.CarAPI_1.core.entity;

public class ProfileEntity {
    private Long id;
    private UserEntity user;
    private String name;
    private Integer budgetMax = 0;
    private Integer usagePurpose = 1;
    private Integer weightPrice = 5;
    private Integer weightSafety = 5;
    private Integer weightReliability = 5;
    private Integer weightEconomy = 5;
    private Integer weightComfort = 5;
    private Integer weightCapacity = 5;
    private Integer weightDynamics = 5;
    private Integer weightAppearance = 5;
    private Integer weightServiceCost = 5;
    private Integer weightFeatures = 5;

    public ProfileEntity() {
    }

    public ProfileEntity(String name) {
        this.name = name;
    }

    public ProfileEntity(String name, UserEntity user) {
        this.name = name;
        this.user = user;
    }

    public ProfileEntity(Long id, UserEntity user, String name, Integer budgetMax,
                         Integer usagePurpose, Integer weightPrice, Integer weightSafety,
                         Integer weightReliability, Integer weightEconomy, Integer weightComfort,
                         Integer weightCapacity, Integer weightDynamics, Integer weightAppearance,
                         Integer weightServiceCost) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.budgetMax = budgetMax;
        this.usagePurpose = usagePurpose;
        this.weightPrice = weightPrice;
        this.weightSafety = weightSafety;
        this.weightReliability = weightReliability;
        this.weightEconomy = weightEconomy;
        this.weightComfort = weightComfort;
        this.weightCapacity = weightCapacity;
        this.weightDynamics = weightDynamics;
        this.weightAppearance = weightAppearance;
        this.weightServiceCost = weightServiceCost;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(Integer budgetMax) {
        this.budgetMax = budgetMax;
    }

    public Integer getUsagePurpose() {
        return usagePurpose;
    }

    public void setUsagePurpose(Integer usagePurpose) {
        this.usagePurpose = usagePurpose;
    }

    public Integer getWeightPrice() {
        return weightPrice;
    }

    public void setWeightPrice(Integer weightPrice) {
        this.weightPrice = weightPrice;
    }

    public Integer getWeightSafety() {
        return weightSafety;
    }

    public void setWeightSafety(Integer weightSafety) {
        this.weightSafety = weightSafety;
    }

    public Integer getWeightReliability() {
        return weightReliability;
    }

    public void setWeightReliability(Integer weightReliability) {
        this.weightReliability = weightReliability;
    }

    public Integer getWeightEconomy() {
        return weightEconomy;
    }

    public void setWeightEconomy(Integer weightEconomy) {
        this.weightEconomy = weightEconomy;
    }

    public Integer getWeightComfort() {
        return weightComfort;
    }

    public void setWeightComfort(Integer weightComfort) {
        this.weightComfort = weightComfort;
    }

    public Integer getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(Integer weightCapacity) {
        this.weightCapacity = weightCapacity;
    }

    public Integer getWeightDynamics() {
        return weightDynamics;
    }

    public void setWeightDynamics(Integer weightDynamics) {
        this.weightDynamics = weightDynamics;
    }

    public Integer getWeightAppearance() {
        return weightAppearance;
    }

    public void setWeightAppearance(Integer weightAppearance) {
        this.weightAppearance = weightAppearance;
    }

    public Integer getWeightServiceCost() {
        return weightServiceCost;
    }

    public void setWeightServiceCost(Integer weightServiceCost) {
        this.weightServiceCost = weightServiceCost;
    }

    public Integer getWeightFeatures() { return weightFeatures; }

    public void getWeightFeatures(Integer weightFeatures) {
        this.weightFeatures = weightFeatures;
    }
}