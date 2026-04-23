package com.example.demo.service;

import com.example.demo.entity.Cars;
import com.example.demo.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CarsService {
    @Autowired
    private CarRepository carRepository;

    //взять одну по id
    public Cars getCarById(long id){

        return carRepository.findById(id).orElse(null);
    }

    //взять все
    public List<Cars> getAllId(){
        return carRepository.findAll();
    }

    //добавить одну с проверкой наличия такой в бд
    public Cars saveCar(Cars newCar){
        List<Cars> carsFromDb = getAllId();
        for (Cars carFromDb : carsFromDb){
            if(carFromDb.getBrand().equals(newCar.getBrand()) &&
                    carFromDb.getModel().equals(newCar.getModel()) &&
                    carFromDb.getGeneration().equals(newCar.getGeneration())){
                return null;
            }
        }
        return carRepository.save(newCar);
    }

    //удалить по id
    public void deleteCarById(long id){
        carRepository.deleteById(id);
    }
}
