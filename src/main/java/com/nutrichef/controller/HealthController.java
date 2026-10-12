package com.nutrichef.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "NutriChef Backend API");
        health.put("version", "1.0.0");
        health.put("timestamp", Instant.now().toString());
        health.put("database", "MySQL / Aiven Cloud");
        health.put("framework", "Spring Boot 3.2.x on Java 17");
        return ResponseEntity.ok(health);
    }
}
