package com.example.CarAPI_1.adapter.web.DTO;

import com.example.CarAPI_1.adapter.database.model.ProfileModel;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDTO {
    private String name;
    private Integer budgetMax;
    private Integer usagePurpose;
    private Integer weightPrice;
    private Integer weightSafety;
    private Integer weightReliability;
    private Integer weightEconomy;
    private Integer weightComfort;
    private Integer weightCapacity;
    private Integer weightDynamics;
    private Integer weightAppearance;
    private Integer weightServiceCost;

    public ProfileDTO(ProfileModel profile){
        this.name = profile.getName();
        this.budgetMax = profile.getBudgetMax();
        this.usagePurpose = profile.getUsagePurpose();
        this.weightPrice = profile.getWeightPrice();
        this.weightSafety = profile.getWeightSafety();
        this.weightReliability = profile.getWeightReliability();
        this.weightEconomy = profile.getWeightEconomy();
        this.weightComfort = profile.getWeightComfort();
        this.weightCapacity = profile.getWeightCapacity();
        this.weightDynamics = profile.getWeightDynamics();
        this.weightAppearance = profile.getWeightAppearance();
        this.weightServiceCost = profile.getWeightServiceCost();
    }

    public ProfileModel toProfileModel(ProfileDTO profileDTO){
        ProfileModel profileModel = new ProfileModel();
        profileModel.setName(profileDTO.name);
        profileModel.setBudgetMax(profileDTO.budgetMax);
        profileModel.setUsagePurpose(profileDTO.usagePurpose);
        profileModel.setWeightSafety(profileDTO.weightSafety);
        profileModel.setWeightReliability(profileDTO.weightReliability);
        profileModel.setWeightEconomy(profileDTO.weightEconomy);
        profileModel.setWeightComfort(profileDTO.weightComfort);
        profileModel.setWeightCapacity(profileDTO.weightCapacity);
        profileModel.setWeightDynamics(profileDTO.weightDynamics);
        profileModel.setWeightAppearance(profileDTO.weightAppearance);
        profileModel.setWeightServiceCost(profileDTO.weightServiceCost);

        return profileModel;
    }

}
