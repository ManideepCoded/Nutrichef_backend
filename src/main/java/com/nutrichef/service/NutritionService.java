package com.nutrichef.service;

import com.nutrichef.dto.MealLogDto;
import com.nutrichef.dto.NutritionSummaryDto;
import com.nutrichef.entity.MealLog;
import com.nutrichef.entity.User;
import com.nutrichef.exception.BadRequestException;
import com.nutrichef.repository.MealLogRepository;
import com.nutrichef.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NutritionService {

    private final MealLogRepository mealLogRepository;
    private final RecipeRepository recipeRepository;
    private final UserService userService;
    private final EdamamNutritionService edamamService;

    public NutritionService(MealLogRepository mealLogRepository,
                            RecipeRepository recipeRepository,
                            UserService userService,
                            EdamamNutritionService edamamService) {
        this.mealLogRepository = mealLogRepository;
        this.recipeRepository = recipeRepository;
        this.userService = userService;
        this.edamamService = edamamService;
    }

    @Transactional(readOnly = true)
    public NutritionSummaryDto getNutritionSummary(LocalDate date) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        List<MealLog> logs = mealLogRepository.findByUserIdAndLogDateOrderByCreatedAtAsc(currentUser.getId(), targetDate);

        int totalCalories = 0;
        double totalProtein = 0.0;
        double totalCarbs = 0.0;
        double totalFat = 0.0;
        Map<String, Integer> mealCalories = new HashMap<>();
        mealCalories.put("BREAKFAST", 0);
        mealCalories.put("LUNCH", 0);
        mealCalories.put("DINNER", 0);
        mealCalories.put("SNACK", 0);

        for (MealLog log : logs) {
            totalCalories += log.getCalories();
            totalProtein += (log.getProteinGrams() != null ? log.getProteinGrams() : 0.0);
            totalCarbs += (log.getCarbsGrams() != null ? log.getCarbsGrams() : 0.0);
            totalFat += (log.getFatGrams() != null ? log.getFatGrams() : 0.0);

            String type = log.getMealType().toUpperCase();
            mealCalories.put(type, mealCalories.getOrDefault(type, 0) + log.getCalories());
        }

        NutritionSummaryDto summary = new NutritionSummaryDto();
        summary.setDate(targetDate);
        summary.setTotalCalories(totalCalories);
        summary.setTargetCalories(currentUser.getDailyCalorieTarget());
        summary.setTotalProtein(Math.round(totalProtein * 10.0) / 10.0);
        summary.setTargetProtein(currentUser.getDailyProteinTarget());
        summary.setTotalCarbs(Math.round(totalCarbs * 10.0) / 10.0);
        summary.setTargetCarbs(currentUser.getDailyCarbsTarget());
        summary.setTotalFat(Math.round(totalFat * 10.0) / 10.0);
        summary.setTargetFat(currentUser.getDailyFatTarget());
        summary.setMealCalories(mealCalories);

        return summary;
    }

    @Transactional
    public MealLogDto logMeal(MealLogDto dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();

        MealLog log = new MealLog();
        log.setUser(currentUser);
        log.setMealName(dto.getMealName().trim());
        log.setMealType(dto.getMealType().toUpperCase().trim());
        log.setLogDate(dto.getLogDate() != null ? dto.getLogDate() : LocalDate.now());

        // If calories are not specified or 0, analyze via Edamam or Demo engine
        if (dto.getCalories() == null || dto.getCalories() <= 0) {
            EdamamNutritionService.NutritionResult estimated = edamamService.analyzeMeal(dto.getMealName());
            log.setCalories(estimated.getCalories());
            log.setProteinGrams(estimated.getProtein());
            log.setCarbsGrams(estimated.getCarbs());
            log.setFatGrams(estimated.getFat());
        } else {
            log.setCalories(dto.getCalories());
            log.setProteinGrams(dto.getProteinGrams() != null ? dto.getProteinGrams() : 0.0);
            log.setCarbsGrams(dto.getCarbsGrams() != null ? dto.getCarbsGrams() : 0.0);
            log.setFatGrams(dto.getFatGrams() != null ? dto.getFatGrams() : 0.0);
        }

        if (dto.getRecipeId() != null) {
            recipeRepository.findById(dto.getRecipeId()).ifPresent(log::setRecipe);
        }

        MealLog saved = mealLogRepository.save(log);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<MealLogDto> getUserMealLogs() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return mealLogRepository.findByUserIdOrderByLogDateDescCreatedAtDesc(currentUser.getId())
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private MealLogDto mapToDto(MealLog log) {
        MealLogDto dto = new MealLogDto();
        dto.setId(log.getId());
        dto.setUserId(log.getUser().getId());
        if (log.getRecipe() != null) {
            dto.setRecipeId(log.getRecipe().getId());
        }
        dto.setMealName(log.getMealName());
        dto.setMealType(log.getMealType());
        dto.setLogDate(log.getLogDate());
        dto.setCalories(log.getCalories());
        dto.setProteinGrams(log.getProteinGrams());
        dto.setCarbsGrams(log.getCarbsGrams());
        dto.setFatGrams(log.getFatGrams());
        dto.setCreatedAt(log.getCreatedAt());
        return dto;
    }
}
