package com.example.CarAPI_1.adapter;

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
public class CarApiAdapter implements CarApiPort {

    @Value("${python.script.path:scripts/python/getCarDetails.py}")
    private String scriptPath;

    @Value("${python.command:python3}")
    private String pythonCommand;

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
}