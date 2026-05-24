package com.example.CarAPI_1.adapter;

import com.example.CarAPI_1.adapter.database.mapper.CarMapper;
import com.example.CarAPI_1.adapter.database.model.CarModel;
import com.example.CarAPI_1.adapter.repository.CarRepository;
import com.example.CarAPI_1.core.entity.CarEntity;
import com.example.CarAPI_1.core.port.CarPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CarAdapter implements CarPort {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarEntity findCarById(Long id){
        CarModel carModel = carRepository.findById(id).orElseThrow();
        CarEntity car = carMapper.toDomain(carModel);
        return car;
    }

    @Override
    public List<CarEntity> findAllCar(){
        List<CarModel> carsModel = carRepository.findAll();
        List<CarEntity> cars = new ArrayList<>();
        for (CarModel carModel : carsModel){
            cars.add(carMapper.toDomain(carModel));
        }
        return cars;
    }

    public void save(CarEntity carEntity){
        CarModel carModel = carMapper.toModel(carEntity);
        carRepository.save(carModel);
    }

    public void deleteCarById(Long id){
        carRepository.deleteById(id);
    }
}
