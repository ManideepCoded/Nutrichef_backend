package com.nutrichef.dto;

public class UserProfileDto {
    private Long id;
    private String email;
    private String fullName;
    private String dietaryPreference;
    private Integer dailyCalorieTarget;
    private Integer dailyProteinTarget;
    private Integer dailyCarbsTarget;
    private Integer dailyFatTarget;

    public UserProfileDto() {}

    public UserProfileDto(Long id, String email, String fullName, String dietaryPreference,
                          Integer dailyCalorieTarget, Integer dailyProteinTarget,
                          Integer dailyCarbsTarget, Integer dailyFatTarget) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.dietaryPreference = dietaryPreference;
        this.dailyCalorieTarget = dailyCalorieTarget;
        this.dailyProteinTarget = dailyProteinTarget;
        this.dailyCarbsTarget = dailyCarbsTarget;
        this.dailyFatTarget = dailyFatTarget;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDietaryPreference() { return dietaryPreference; }
    public void setDietaryPreference(String dietaryPreference) { this.dietaryPreference = dietaryPreference; }

    public Integer getDailyCalorieTarget() { return dailyCalorieTarget; }
    public void setDailyCalorieTarget(Integer dailyCalorieTarget) { this.dailyCalorieTarget = dailyCalorieTarget; }

    public Integer getDailyProteinTarget() { return dailyProteinTarget; }
    public void setDailyProteinTarget(Integer dailyProteinTarget) { this.dailyProteinTarget = dailyProteinTarget; }

    public Integer getDailyCarbsTarget() { return dailyCarbsTarget; }
    public void setDailyCarbsTarget(Integer dailyCarbsTarget) { this.dailyCarbsTarget = dailyCarbsTarget; }

    public Integer getDailyFatTarget() { return dailyFatTarget; }
    public void setDailyFatTarget(Integer dailyFatTarget) { this.dailyFatTarget = dailyFatTarget; }
}
