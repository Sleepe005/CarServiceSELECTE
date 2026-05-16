package com.example.CarAPI_1.core.port;

import com.example.CarAPI_1.core.entity.CarEntity;

import java.util.List;

public interface CarPort {
    public CarEntity findCarById(Long id);
    public List<CarEntity> findAllCar();
    public void save(CarEntity carEntity);
    public void deleteCarById(Long id);
}
