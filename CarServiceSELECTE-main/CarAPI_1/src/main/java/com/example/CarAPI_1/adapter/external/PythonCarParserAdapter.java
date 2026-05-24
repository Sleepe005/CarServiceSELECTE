package com.example.CarAPI_1.adapter.external;

import com.example.CarAPI_1.core.entity.CarEntity;
import com.example.CarAPI_1.core.port.CarApiPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PythonCarParserAdapter implements CarApiPort {

    @Value("${python.script.path:scripts/python/getCarDetails.py}")
    private String scriptPath;

    @Value("${python.command:python}")
    private String pythonCommand;

    @Override
    public List<CarEntity> loadCars() {
        log.info("🚀 Запуск Python парсера для массового сбора автомобилей");

        try {
            String projectDir = System.getProperty("user.dir");
            log.info("📂 Рабочая директория: {}", projectDir);

            // Полный путь к скрипту
            String fullScriptPath = projectDir + File.separator + scriptPath;
            log.info("📜 Путь к скрипту: {}", fullScriptPath);

            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonCommand,
                    fullScriptPath
            );

            processBuilder.directory(new File(projectDir));
            processBuilder.redirectErrorStream(true);

            log.info("💻 Команда: {}", String.join(" ", processBuilder.command()));

            Process process = processBuilder.start();

            // Читаем вывод Python
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[PYTHON] {}", line);
                }
            }

            // Ждем завершения (до 30 минут)
            boolean finished = process.waitFor(30, TimeUnit.MINUTES);

            if (finished) {
                int exitCode = process.exitValue();
                if (exitCode == 0) {
                    log.info("✅ Python парсер успешно завершил работу");
                } else {
                    log.error("❌ Python парсер завершился с ошибкой, код: {}", exitCode);
                }
            } else {
                process.destroyForcibly();
                log.error("❌ Python парсер превысил время выполнения (30 минут)");
            }

        } catch (Exception e) {
            log.error("❌ Ошибка при запуске Python: {}", e.getMessage(), e);
        }

        return new ArrayList<>();
    }
}