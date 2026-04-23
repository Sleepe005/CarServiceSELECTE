package com.example.demo.service;

import com.example.demo.entity.Cars;
import com.example.demo.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarsServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarsService carsService;

    private Cars createTestCar(Long id, String brand, String model) {
        Cars car = new Cars();
        car.setId(id);
        car.setBrand(brand);
        car.setModel(model);
        car.setGeneration("VII");
        car.setYear_from(LocalDate.of(2020, 1, 1));
        car.setYear_to(LocalDate.of(2025, 12, 31));
        car.setPrice(3500000L);
        car.setSafety_rating(5L);
        car.setReliability_rating(4L);
        car.setFuel_consumption(8L);
        car.setMaintenance_cost(50000L);
        car.setComfort_rating(5L);
        car.setCapacity_passengers(5L);
        car.setCapacity_baggage(400L);
        car.setAcceleration_0_100(7L);
        car.setAppearance_rating(4L);
        car.setBody_type("Sedan");
        car.setFuel_type("Petrol");
        car.setTransmission("Automatic");
        car.setDrivetrain("FWD");
        car.setPower_hp(150L);
        car.setAdditional_options("Climate control, heated seats");
        car.setLast_updated(LocalDate.now());
        return car;
    }

    @Test
    void getCarById_WhenCarExists_ShouldReturnCar() {
        // Подготовка
        Long carId = 1L;
        Cars expectedCar = createTestCar(carId, "Toyota", "Camry");

        when(carRepository.findById(carId)).thenReturn(Optional.of(expectedCar));

        // Действие
        Cars result = carsService.getCarById(carId);

        // Проверка
        assertNotNull(result);
        assertEquals(carId, result.getId());
        assertEquals("Toyota", result.getBrand());
        assertEquals("Camry", result.getModel());
        assertEquals("Sedan", result.getBody_type());
        assertEquals(3500000L, result.getPrice());
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void getCarById_WhenCarDoesNotExist_ShouldReturnNull() {
        // Подготовка
        Long carId = 999L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Действие
        Cars result = carsService.getCarById(carId);

        // Проверка
        assertNull(result);
        verify(carRepository, times(1)).findById(carId);
    }

    @Test
    void getAllId_ShouldReturnListOfAllCars() {
        // Подготовка
        Cars car1 = createTestCar(1L, "Toyota", "Camry");
        Cars car2 = createTestCar(2L, "Honda", "Civic");
        Cars car3 = createTestCar(5L, "BMW", "X5");

        List<Cars> carsList = Arrays.asList(car1, car2, car3);
        when(carRepository.findAll()).thenReturn(carsList);

        // Действие
        List<Cars> result = carsService.getAllId();

        // Проверка
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Toyota", result.get(0).getBrand());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Honda", result.get(1).getBrand());
        assertEquals(5L, result.get(2).getId());
        assertEquals("BMW", result.get(2).getBrand());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getAllId_WhenDatabaseIsEmpty_ShouldReturnEmptyList() {
        // Подготовка
        when(carRepository.findAll()).thenReturn(Arrays.asList());

        // Действие
        List<Cars> result = carsService.getAllId();

        // Проверка
        assertTrue(result.isEmpty());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void saveCar_WhenCarDoesNotExist_ShouldSaveAndReturnCar() {
        // Подготовка
        Cars newCar = createTestCar(null, "Tesla", "Model 3");
        Cars savedCar = createTestCar(1L, "Tesla", "Model 3");

        when(carRepository.findAll()).thenReturn(Arrays.asList());
        when(carRepository.save(newCar)).thenReturn(savedCar);

        // Действие
        Cars result = carsService.saveCar(newCar);

        // Проверка
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Tesla", result.getBrand());
        assertEquals("Model 3", result.getModel());
        verify(carRepository, times(1)).findAll();
        verify(carRepository, times(1)).save(newCar);
    }

    @Test
    void saveCar_WhenCarAlreadyExists_ShouldReturnNull() {
        // Подготовка
        Cars existingCar = createTestCar(1L, "Toyota", "Camry");
        Cars sameCar = createTestCar(null, "Toyota", "Camry");

        when(carRepository.findAll()).thenReturn(Arrays.asList(existingCar));

        // Действие
        Cars result = carsService.saveCar(sameCar);

        // Проверка
        assertNull(result);
        verify(carRepository, times(1)).findAll();
        verify(carRepository, never()).save(any());
    }

    @Test
    void deleteCarById_ShouldCallRepositoryDeleteMethod() {
        // Подготовка
        Long carId = 5L;
        doNothing().when(carRepository).deleteById(carId);

        // Действие
        carsService.deleteCarById(carId);

        // Проверка
        verify(carRepository, times(1)).deleteById(carId);
    }
}