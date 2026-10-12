package com.nutrichef.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "ingredient_name", nullable = false, length = 100)
    private String ingredientName;

    @Column(name = "normalized_name", nullable = false, length = 100)
    private String normalizedName;

    @Column
    private Double quantity = 1.0;

    @Column(length = 20)
    private String unit = "unit";

    @Column(name = "is_optional")
    private Boolean isOptional = false;

    public RecipeIngredient() {}

    public RecipeIngredient(Recipe recipe, String ingredientName, String normalizedName, Double quantity, String unit, Boolean isOptional) {
        this.recipe = recipe;
        this.ingredientName = ingredientName;
        this.normalizedName = normalizedName;
        this.quantity = quantity;
        this.unit = unit;
        this.isOptional = isOptional;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public String getNormalizedName() { return normalizedName; }
    public void setNormalizedName(String normalizedName) { this.normalizedName = normalizedName; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Boolean getIsOptional() { return isOptional; }
    public void setIsOptional(Boolean optional) { isOptional = optional; }
}
