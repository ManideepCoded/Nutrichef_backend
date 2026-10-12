package com.nutrichef.controller;

import com.nutrichef.dto.MealLogDto;
import com.nutrichef.dto.NutritionSummaryDto;
import com.nutrichef.service.NutritionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping("/summary")
    public ResponseEntity<NutritionSummaryDto> getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        NutritionSummaryDto summary = nutritionService.getNutritionSummary(date);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/meal-logs")
    public ResponseEntity<MealLogDto> logMeal(@Valid @RequestBody MealLogDto dto) {
        MealLogDto logged = nutritionService.logMeal(dto);
        return new ResponseEntity<>(logged, HttpStatus.CREATED);
    }

    @GetMapping("/meal-logs")
    public ResponseEntity<List<MealLogDto>> getMealLogs() {
        List<MealLogDto> logs = nutritionService.getUserMealLogs();
        return ResponseEntity.ok(logs);
    }
}
