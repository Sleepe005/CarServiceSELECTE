// adapter/persistence/mapper/CarMapper.java
package com.example.CarAPI_1.adapter.database.mapper;

import com.example.CarAPI_1.core.entity.CarEntity;
import com.example.CarAPI_1.adapter.database.model.CarModel;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    // Car (core) → CarModel (adapter)
    public CarModel toModel(CarEntity domain) {
        if (domain == null) {
            return null;
        }

        CarModel model = new CarModel();
        model.setId(domain.getId());
        model.setBrand(domain.getBrand());
        model.setModel(domain.getModel());
        model.setGeneration(domain.getGeneration());
        model.setYearFrom(domain.getYearFrom());
        model.setYearTo(domain.getYearTo());
        model.setPrice(domain.getPrice());
        model.setBodyType(domain.getBodyType());
        model.setFuelType(domain.getFuelType());
        model.setTransmission(domain.getTransmission());
        model.setDrivetrain(domain.getDrivetrain());
        model.setPowerHp(domain.getPowerHp());
        model.setSafetyRating(domain.getSafetyRating());
        model.setReliabilityRating(domain.getReliabilityRating());
        model.setEconomyRating(domain.getEconomyRating());
        model.setComfortRating(domain.getComfortRating());
        model.setCapacityRating(domain.getCapacityRating());
        model.setDynamicsRating(domain.getDynamicsRating());
        model.setAppearanceRating(domain.getAppearanceRating());
        model.setFeaturesRating(domain.getFeaturesRating());

        return model;
    }

    // CarModel (adapter) → Car (core)
    public CarEntity toDomain(CarModel model) {
        if (model == null) {
            return null;
        }

        return new CarEntity(
                model.getId(),
                model.getBrand(),
                model.getModel(),
                model.getGeneration(),
                model.getYearFrom(),
                model.getYearTo(),
                model.getPrice(),
                model.getBodyType(),
                model.getFuelType(),
                model.getTransmission(),
                model.getDrivetrain(),
                model.getPowerHp(),
                model.getSafetyRating(),
                model.getReliabilityRating(),
                model.getEconomyRating(),
                model.getComfortRating(),
                model.getCapacityRating(),
                model.getDynamicsRating(),
                model.getAppearanceRating(),
                model.getFeaturesRating()
        );
    }

    // Обновление существующей CarModel из Car (для update операций)
    public void updateModel(CarModel model, CarEntity domain) {
        if (domain == null || model == null) {
            return;
        }

        model.setBrand(domain.getBrand());
        model.setModel(domain.getModel());
        model.setGeneration(domain.getGeneration());
        model.setYearFrom(domain.getYearFrom());
        model.setYearTo(domain.getYearTo());
        model.setPrice(domain.getPrice());
        model.setBodyType(domain.getBodyType());
        model.setFuelType(domain.getFuelType());
        model.setTransmission(domain.getTransmission());
        model.setDrivetrain(domain.getDrivetrain());
        model.setPowerHp(domain.getPowerHp());
        model.setSafetyRating(domain.getSafetyRating());
        model.setReliabilityRating(domain.getReliabilityRating());
        model.setEconomyRating(domain.getEconomyRating());
        model.setComfortRating(domain.getComfortRating());
        model.setCapacityRating(domain.getCapacityRating());
        model.setDynamicsRating(domain.getDynamicsRating());
        model.setAppearanceRating(domain.getAppearanceRating());
        model.setFeaturesRating(domain.getFeaturesRating());
        // id не обновляем
    }
}