package com.example.CarAPI_1.core.port;
import com.example.CarAPI_1.core.entity.CarEntity;
import java.util.List;

public interface CarApiPort {
    List<CarEntity> loadCars();  // Загружает машины из внешнего источника
}