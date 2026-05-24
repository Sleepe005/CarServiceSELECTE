package com.example.CarAPI_1.adapter.web;

import com.example.CarAPI_1.core.port.CarApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/parser")
@RequiredArgsConstructor
public class PythonParserController {

    private final CarApiPort carApiPort;

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startParser() {
        // Запускаем в отдельном потоке, чтобы не блокировать ответ
        System.out.println("===== КОНТРОЛЛЕР ВЫЗВАН =====");
        new Thread(() -> carApiPort.loadCars()).start();

        Map<String, String> response = new HashMap<>();
        response.put("status", "started");
        response.put("message", "Парсинг запущен в фоновом режиме. Смотри логи.");
        return ResponseEntity.ok(response);
    }
}