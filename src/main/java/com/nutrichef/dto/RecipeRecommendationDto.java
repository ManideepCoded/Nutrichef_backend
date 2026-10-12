package com.nutrichef.dto;

import java.util.List;

public class RecipeRecommendationDto {
    private RecipeDto recipe;
    private Double matchPercentage;
    private Integer availableCount;
    private Integer totalRequiredCount;
    private List<String> matchingIngredients;
    private List<String> missingIngredients;
    private Boolean usesExpiringIngredients;
    private List<String> expiringIngredientNames;

    public RecipeRecommendationDto() {}

    public RecipeDto getRecipe() { return recipe; }
    public void setRecipe(RecipeDto recipe) { this.recipe = recipe; }

    public Double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(Double matchPercentage) { this.matchPercentage = matchPercentage; }

    public Integer getAvailableCount() { return availableCount; }
    public void setAvailableCount(Integer availableCount) { this.availableCount = availableCount; }

    public Integer getTotalRequiredCount() { return totalRequiredCount; }
    public void setTotalRequiredCount(Integer totalRequiredCount) { this.totalRequiredCount = totalRequiredCount; }

    public List<String> getMatchingIngredients() { return matchingIngredients; }
    public void setMatchingIngredients(List<String> matchingIngredients) { this.matchingIngredients = matchingIngredients; }

    public List<String> getMissingIngredients() { return missingIngredients; }
    public void setMissingIngredients(List<String> missingIngredients) { this.missingIngredients = missingIngredients; }

    public Boolean getUsesExpiringIngredients() { return usesExpiringIngredients; }
    public void setUsesExpiringIngredients(Boolean usesExpiringIngredients) { this.usesExpiringIngredients = usesExpiringIngredients; }

    public List<String> getExpiringIngredientNames() { return expiringIngredientNames; }
    public void setExpiringIngredientNames(List<String> expiringIngredientNames) { this.expiringIngredientNames = expiringIngredientNames; }
}
