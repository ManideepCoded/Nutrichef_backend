package com.nutrichef.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EdamamNutritionService {

    private static final Logger log = LoggerFactory.getLogger(EdamamNutritionService.class);

    @Value("${nutrichef.edamam.app-id:}")
    private String appId;

    @Value("${nutrichef.edamam.app-key:}")
    private String appKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public static class NutritionResult {
        private final int calories;
        private final double protein;
        private final double carbs;
        private final double fat;
        private final boolean isDemoMode;

        public NutritionResult(int calories, double protein, double carbs, double fat, boolean isDemoMode) {
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
            this.isDemoMode = isDemoMode;
        }

        public int getCalories() { return calories; }
        public double getProtein() { return protein; }
        public double getCarbs() { return carbs; }
        public double getFat() { return fat; }
        public boolean isDemoMode() { return isDemoMode; }
    }

    public NutritionResult analyzeMeal(String query) {
        if (appId == null || appId.isBlank() || appKey == null || appKey.isBlank()) {
            log.info("Edamam credentials not configured. Using NutriChef internal Demo Nutrition Engine for: '{}'", query);
            return calculateDemoNutrition(query);
        }

        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = String.format(
                    "https://api.edamam.com/api/nutrition-data?app_id=%s&app_key=%s&ingr=%s",
                    appId, appKey, encoded
            );

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map body = response.getBody();

            if (body != null && body.containsKey("calories")) {
                int calories = ((Number) body.get("calories")).intValue();
                Map totalNutrients = (Map) body.get("totalNutrients");

                double protein = 0.0;
                double carbs = 0.0;
                double fat = 0.0;

                if (totalNutrients != null) {
                    if (totalNutrients.containsKey("PROCNT")) {
                        protein = ((Number) ((Map) totalNutrients.get("PROCNT")).get("quantity")).doubleValue();
                    }
                    if (totalNutrients.containsKey("CHOCDF")) {
                        carbs = ((Number) ((Map) totalNutrients.get("CHOCDF")).get("quantity")).doubleValue();
                    }
                    if (totalNutrients.containsKey("FAT")) {
                        fat = ((Number) ((Map) totalNutrients.get("FAT")).get("quantity")).doubleValue();
                    }
                }

                return new NutritionResult(calories, round(protein), round(carbs), round(fat), false);
            }
        } catch (Exception e) {
            log.warn("Edamam API call failed ({}). Gracefully falling back to Demo Nutrition Engine.", e.getMessage());
        }

        return calculateDemoNutrition(query);
    }

    private NutritionResult calculateDemoNutrition(String query) {
        String lower = query.toLowerCase();
        int baseCalories = 250;
        double protein = 12.0;
        double carbs = 30.0;
        double fat = 8.0;

        if (lower.contains("chicken") || lower.contains("turkey") || lower.contains("steak") || lower.contains("beef")) {
            baseCalories = 420;
            protein = 42.0;
            carbs = 4.0;
            fat = 22.0;
        } else if (lower.contains("salad") || lower.contains("vegetable") || lower.contains("greens")) {
            baseCalories = 180;
            protein = 6.0;
            carbs = 18.0;
            fat = 9.0;
        } else if (lower.contains("pasta") || lower.contains("rice") || lower.contains("quinoa") || lower.contains("bowl")) {
            baseCalories = 450;
            protein = 15.0;
            carbs = 72.0;
            fat = 10.0;
        } else if (lower.contains("egg") || lower.contains("omelette")) {
            baseCalories = 320;
            protein = 24.0;
            carbs = 6.0;
            fat = 22.0;
        } else if (lower.contains("salmon") || lower.contains("fish") || lower.contains("tuna")) {
            baseCalories = 390;
            protein = 36.0;
            carbs = 0.0;
            fat = 24.0;
        } else if (lower.contains("smoothie") || lower.contains("shake") || lower.contains("yogurt")) {
            baseCalories = 260;
            protein = 20.0;
            carbs = 35.0;
            fat = 4.0;
        }

        return new NutritionResult(baseCalories, protein, carbs, fat, true);
    }

    private double round(double val) {
        return Math.round(val * 10.0) / 10.0;
    }
}
