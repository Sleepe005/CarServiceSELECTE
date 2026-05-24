package com.example.CarAPI_1.adapter.repository;

import com.example.CarAPI_1.adapter.database.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarModel, Long> {
}
