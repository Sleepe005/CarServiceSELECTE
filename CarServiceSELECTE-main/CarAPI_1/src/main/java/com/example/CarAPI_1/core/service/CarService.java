package com.example.CarAPI_1.core.service;

import com.example.CarAPI_1.core.entity.*;
import com.example.CarAPI_1.core.port.*;

import java.util.List;

public class CarService {
    private final CarPort carPort;
    public CarService(CarPort carPort) {
        this.carPort = carPort;
    }

    //добавить одну с проверкой наличия такой в бд
    public void saveCar(CarEntity newCarEntity){
        List<CarEntity> carsEntityFromDb = carPort.findAllCar();
        for (CarEntity carEntityFromDb : carsEntityFromDb){
            if(carEntityFromDb.getBrand().equals(newCarEntity.getBrand()) &&
                    carEntityFromDb.getModel().equals(newCarEntity.getModel()) &&
                    carEntityFromDb.getGeneration().equals(newCarEntity.getGeneration())){
               break;
            }
        }

        carPort.save(newCarEntity);
    }
}
