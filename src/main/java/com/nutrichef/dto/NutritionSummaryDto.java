package com.nutrichef.dto;

import java.time.LocalDate;
import java.util.Map;

public class NutritionSummaryDto {
    private LocalDate date;
    private Integer totalCalories;
    private Integer targetCalories;
    private Double totalProtein;
    private Integer targetProtein;
    private Double totalCarbs;
    private Integer targetCarbs;
    private Double totalFat;
    private Integer targetFat;
    private Map<String, Integer> mealCalories;

    public NutritionSummaryDto() {}

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getTotalCalories() { return totalCalories; }
    public void setTotalCalories(Integer totalCalories) { this.totalCalories = totalCalories; }

    public Integer getTargetCalories() { return targetCalories; }
    public void setTargetCalories(Integer targetCalories) { this.targetCalories = targetCalories; }

    public Double getTotalProtein() { return totalProtein; }
    public void setTotalProtein(Double totalProtein) { this.totalProtein = totalProtein; }

    public Integer getTargetProtein() { return targetProtein; }
    public void setTargetProtein(Integer targetProtein) { this.targetProtein = targetProtein; }

    public Double getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(Double totalCarbs) { this.totalCarbs = totalCarbs; }

    public Integer getTargetCarbs() { return targetCarbs; }
    public void setTargetCarbs(Integer targetCarbs) { this.targetCarbs = targetCarbs; }

    public Double getTotalFat() { return totalFat; }
    public void setTotalFat(Double totalFat) { this.totalFat = totalFat; }

    public Integer getTargetFat() { return targetFat; }
    public void setTargetFat(Integer targetFat) { this.targetFat = targetFat; }

    public Map<String, Integer> getMealCalories() { return mealCalories; }
    public void setMealCalories(Map<String, Integer> mealCalories) { this.mealCalories = mealCalories; }
}
