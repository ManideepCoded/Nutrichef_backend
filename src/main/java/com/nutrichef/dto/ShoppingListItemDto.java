package com.nutrichef.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ShoppingListItemDto {
    private Long id;

    @NotBlank(message = "Item name is required")
    private String name;

    private String category = "GROCERY";

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Double quantity = 1.0;

    private String unit = "pcs";
    private Boolean isCompleted = false;
    private Long sourceRecipeId;
    private String sourceRecipeTitle;
    private LocalDateTime createdAt;

    public ShoppingListItemDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean completed) { isCompleted = completed; }

    public Long getSourceRecipeId() { return sourceRecipeId; }
    public void setSourceRecipeId(Long sourceRecipeId) { this.sourceRecipeId = sourceRecipeId; }

    public String getSourceRecipeTitle() { return sourceRecipeTitle; }
    public void setSourceRecipeTitle(String sourceRecipeTitle) { this.sourceRecipeTitle = sourceRecipeTitle; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
