package com.example.CarAPI_1.repository;

import com.example.CarAPI_1.model.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Cars, Long> {
}
