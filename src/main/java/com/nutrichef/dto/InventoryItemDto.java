package com.nutrichef.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class InventoryItemDto {
    private Long id;

    @NotBlank(message = "Item name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Double quantity;

    private String unit = "pcs";

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    private String storageLocation = "FRIDGE";
    private String notes;

    private Long daysUntilExpiry;
    private Boolean isExpiringSoon = false;
    private Boolean isExpired = false;

    public InventoryItemDto() {}

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

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getDaysUntilExpiry() { return daysUntilExpiry; }
    public void setDaysUntilExpiry(Long daysUntilExpiry) { this.daysUntilExpiry = daysUntilExpiry; }

    public Boolean getIsExpiringSoon() { return isExpiringSoon; }
    public void setIsExpiringSoon(Boolean expiringSoon) { isExpiringSoon = expiringSoon; }

    public Boolean getIsExpired() { return isExpired; }
    public void setIsExpired(Boolean expired) { isExpired = expired; }
}
