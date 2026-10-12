package com.nutrichef.dto;

import java.util.List;

public class DashboardSummaryDto {
    private long totalInventoryItems;
    private long expiringSoonItems;
    private long expiredItems;
    private long pendingShoppingItems;
    private int todayCaloriesConsumed;
    private int dailyCalorieTarget;
    private double todayProteinConsumed;
    private double todayCarbsConsumed;
    private double todayFatConsumed;
    private List<RecipeRecommendationDto> topRecommendations;

    public DashboardSummaryDto() {}

    public long getTotalInventoryItems() { return totalInventoryItems; }
    public void setTotalInventoryItems(long totalInventoryItems) { this.totalInventoryItems = totalInventoryItems; }

    public long getExpiringSoonItems() { return expiringSoonItems; }
    public void setExpiringSoonItems(long expiringSoonItems) { this.expiringSoonItems = expiringSoonItems; }

    public long getExpiredItems() { return expiredItems; }
    public void setExpiredItems(long expiredItems) { this.expiredItems = expiredItems; }

    public long getPendingShoppingItems() { return pendingShoppingItems; }
    public void setPendingShoppingItems(long pendingShoppingItems) { this.pendingShoppingItems = pendingShoppingItems; }

    public int getTodayCaloriesConsumed() { return todayCaloriesConsumed; }
    public void setTodayCaloriesConsumed(int todayCaloriesConsumed) { this.todayCaloriesConsumed = todayCaloriesConsumed; }

    public int getDailyCalorieTarget() { return dailyCalorieTarget; }
    public void setDailyCalorieTarget(int dailyCalorieTarget) { this.dailyCalorieTarget = dailyCalorieTarget; }

    public double getTodayProteinConsumed() { return todayProteinConsumed; }
    public void setTodayProteinConsumed(double todayProteinConsumed) { this.todayProteinConsumed = todayProteinConsumed; }

    public double getTodayCarbsConsumed() { return todayCarbsConsumed; }
    public void setTodayCarbsConsumed(double todayCarbsConsumed) { this.todayCarbsConsumed = todayCarbsConsumed; }

    public double getTodayFatConsumed() { return todayFatConsumed; }
    public void setTodayFatConsumed(double todayFatConsumed) { this.todayFatConsumed = todayFatConsumed; }

    public List<RecipeRecommendationDto> getTopRecommendations() { return topRecommendations; }
    public void setTopRecommendations(List<RecipeRecommendationDto> topRecommendations) { this.topRecommendations = topRecommendations; }
}
