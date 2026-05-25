package com.example.CarAPI_1.adapter;

import com.example.CarAPI_1.adapter.database.model.CarModel;
import com.example.CarAPI_1.adapter.repository.CarRepository;
import com.example.CarAPI_1.adapter.web.DTO.CarImportDTO;
import com.example.CarAPI_1.core.entity.CarEntity;
import com.example.CarAPI_1.core.port.CarApiPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class CarApiAdapter implements CarApiPort {

    private final CarRepository carRepository;

    @Value("${python.script.path:scripts/python/getCarDetails.py}")
    private String scriptPath;

    @Value("${python.command:python3}")
    private String pythonCommand;

    private static final String JSON_FILE_PATH = "cars_full_output.json";

    private final ObjectMapper objectMapper;

    @Override
    public List<CarEntity> loadCars() {
        log.info("🚀 Запуск Python парсера");

        try {
            String projectDir = System.getProperty("user.dir");
            String fullScriptPath = projectDir + File.separator + scriptPath;

            // ✅ Проверяем существование скрипта
            File scriptFile = new File(fullScriptPath);
            log.info("📂 Путь к скрипту: {}", fullScriptPath);
            log.info("📄 Файл существует: {}", scriptFile.exists());
            log.info("📄 Файл читаемый: {}", scriptFile.canRead());

            if (!scriptFile.exists()) {
                log.error("❌ Скрипт не найден!");
                return new ArrayList<>();
            }

            // ✅ Проверяем Python
            ProcessBuilder checkPython = new ProcessBuilder(pythonCommand, "--version");
            Process pythonProcess = checkPython.start();
            int pythonExitCode = pythonProcess.waitFor();
            log.info("🐍 Python доступен, exit code: {}", pythonExitCode);

            // ✅ Запускаем скрипт
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonCommand,
                    fullScriptPath
            );
            processBuilder.directory(new File(projectDir));
            processBuilder.redirectErrorStream(true);

            log.info("💻 Команда: {}", String.join(" ", processBuilder.command()));
            Process process = processBuilder.start();

            // Читаем вывод построчно в реальном времени
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[PYTHON] {}", line);
                }
            }

            int exitCode = process.waitFor();
            log.info("✅ Python скрипт завершился с кодом: {}", exitCode);

            if (exitCode != 0) {
                log.error("❌ Скрипт завершился с ошибкой!");
            }

        } catch (Exception e) {
            log.error("❌ Ошибка: {}", e.getMessage(), e);
        }

        return new ArrayList<>();
    }

    @Transactional
    public int importCarsFromJson() {
        log.info("🚗 Начинаем импорт автомобилей из JSON файла: {}", JSON_FILE_PATH);

        try {
            // 1. Читаем JSON файл
            File jsonFile = new File(JSON_FILE_PATH);
            if (!jsonFile.exists()) {
                log.error("❌ Файл не найден: {}", jsonFile.getAbsolutePath());
                return 0;
            }

            List<CarImportDTO> importCars = objectMapper.readValue(
                    jsonFile,
                    new TypeReference<List<CarImportDTO>>() {}
            );

            log.info("📄 Прочитано {} записей из JSON", importCars.size());

            // 2. Преобразуем и сохраняем
            List<CarModel> carsToSave = new ArrayList<>();
            int skippedCount = 0;

            for (CarImportDTO dto : importCars) {
                try {
                    CarModel car = convertToCarModel(dto);
                    carsToSave.add(car);
                } catch (Exception e) {
                    log.warn("⚠️ Пропущена запись {} {}: {}", dto.getBrand(), dto.getModel(), e.getMessage());
                    skippedCount++;
                }
            }

            // 3. Сохраняем в базу
            if (!carsToSave.isEmpty()) {
                carRepository.saveAll(carsToSave);
                log.info("✅ Импортировано {} автомобилей", carsToSave.size());
            }

            if (skippedCount > 0) {
                log.warn("⚠️ Пропущено {} записей", skippedCount);
            }

            return carsToSave.size();

        } catch (IOException e) {
            log.error("❌ Ошибка при чтении JSON файла: {}", e.getMessage(), e);
            return 0;
        }
    }

    private CarModel convertToCarModel(CarImportDTO dto) {
        CarModel car = new CarModel();

        // Основные поля
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setGeneration(dto.getGeneration());

        // Годы выпуска (Integer -> LocalDate)
        if (dto.getYearFrom() != null) {
            car.setYearFrom(LocalDate.of(dto.getYearFrom(), 1, 1));
        } else {
            car.setYearFrom(LocalDate.of(2000, 1, 1)); // значение по умолчанию
        }

        if (dto.getYearTo() != null) {
            car.setYearTo(LocalDate.of(dto.getYearTo(), 12, 31));
        } else {
            car.setYearTo(LocalDate.of(2025, 12, 31)); // значение по умолчанию
        }

        // Технические характеристики
        car.setBodyType(dto.getBodyType() != null ? dto.getBodyType() : "Sedan");
        car.setFuelType(dto.getFuelType() != null ? dto.getFuelType() : "Petrol");
        car.setTransmission(dto.getTransmission() != null ? dto.getTransmission() : "Automatic");
        car.setDrivetrain(dto.getDrivetrain() != null ? dto.getDrivetrain() : "FWD");
        car.setPowerHp(dto.getPowerHp() != null ? Long.valueOf(dto.getPowerHp()) : 100L);

        // Цена (пока заглушка, можно потом дополнить)
        car.setPrice(0L);

        // Рейтинги
        if (dto.getRatings() != null) {
            car.setSafetyRating(dto.getRatings().getSafety() != null ? Long.valueOf(dto.getRatings().getSafety()) : 5L);
            car.setReliabilityRating(dto.getRatings().getReliability() != null ? Long.valueOf(dto.getRatings().getReliability()) : 5L);
            car.setEconomyRating(dto.getRatings().getEconomy() != null ? Long.valueOf(dto.getRatings().getEconomy()) : 5L);
            car.setComfortRating(dto.getRatings().getComfort() != null ? Long.valueOf(dto.getRatings().getComfort()) : 5L);
            car.setCapacityRating(dto.getRatings().getCapacity() != null ? Long.valueOf(dto.getRatings().getCapacity()) : 5L);
            car.setDynamicsRating(dto.getRatings().getDynamics() != null ? Long.valueOf(dto.getRatings().getDynamics()) : 5L);
            car.setAppearanceRating(dto.getRatings().getAppearance() != null ? Long.valueOf(dto.getRatings().getAppearance()) : 5L);
            car.setFeaturesRating(dto.getRatings().getFeatures() != null ? Long.valueOf(dto.getRatings().getFeatures()) : 5L);
        } else {
            // Значения по умолчанию
            car.setSafetyRating(5L);
            car.setReliabilityRating(5L);
            car.setEconomyRating(5L);
            car.setComfortRating(5L);
            car.setCapacityRating(5L);
            car.setDynamicsRating(5L);
            car.setAppearanceRating(5L);
            car.setFeaturesRating(5L);
        }

        return car;
    }
}