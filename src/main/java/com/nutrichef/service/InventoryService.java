package com.nutrichef.service;

import com.nutrichef.dto.InventoryItemDto;
import com.nutrichef.entity.InventoryItem;
import com.nutrichef.entity.User;
import com.nutrichef.exception.ResourceNotFoundException;
import com.nutrichef.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryRepository;
    private final UserService userService;

    public InventoryService(InventoryItemRepository inventoryRepository, UserService userService) {
        this.inventoryRepository = inventoryRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<InventoryItemDto> getUserInventory(String category) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        List<InventoryItem> items;
        if (category != null && !category.isBlank()) {
            items = inventoryRepository.findByUserIdAndCategoryOrderByExpiryDateAsc(currentUser.getId(), category);
        } else {
            items = inventoryRepository.findByUserIdOrderByExpiryDateAsc(currentUser.getId());
        }
        return items.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public InventoryItemDto addInventoryItem(InventoryItemDto dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();

        InventoryItem item = new InventoryItem();
        item.setUser(currentUser);
        item.setName(dto.getName().trim());
        item.setCategory(dto.getCategory().toUpperCase().trim());
        item.setQuantity(dto.getQuantity());
        item.setUnit(dto.getUnit() != null ? dto.getUnit().trim() : "pcs");
        item.setExpiryDate(dto.getExpiryDate());
        item.setStorageLocation(dto.getStorageLocation() != null ? dto.getStorageLocation().toUpperCase().trim() : "FRIDGE");
        item.setNotes(dto.getNotes());

        InventoryItem saved = inventoryRepository.save(item);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public InventoryItemDto getInventoryItem(Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        InventoryItem item = inventoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item with ID " + id + " not found or unauthorized"));
        return mapToDto(item);
    }

    @Transactional
    public InventoryItemDto updateInventoryItem(Long id, InventoryItemDto dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        InventoryItem item = inventoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item with ID " + id + " not found or unauthorized"));

        item.setName(dto.getName().trim());
        item.setCategory(dto.getCategory().toUpperCase().trim());
        item.setQuantity(dto.getQuantity());
        if (dto.getUnit() != null) item.setUnit(dto.getUnit().trim());
        item.setExpiryDate(dto.getExpiryDate());
        if (dto.getStorageLocation() != null) item.setStorageLocation(dto.getStorageLocation().toUpperCase().trim());
        item.setNotes(dto.getNotes());

        InventoryItem updated = inventoryRepository.save(item);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteInventoryItem(Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        InventoryItem item = inventoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item with ID " + id + " not found or unauthorized"));
        inventoryRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public List<InventoryItemDto> getExpiringSoonItems() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(3);
        List<InventoryItem> items = inventoryRepository.findExpiringSoon(currentUser.getId(), today, threshold);
        return items.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryItemDto> getExpiredItems() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        LocalDate today = LocalDate.now();
        List<InventoryItem> items = inventoryRepository.findExpired(currentUser.getId(), today);
        return items.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public InventoryItemDto mapToDto(InventoryItem item) {
        InventoryItemDto dto = new InventoryItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setCategory(item.getCategory());
        dto.setQuantity(item.getQuantity());
        dto.setUnit(item.getUnit());
        dto.setExpiryDate(item.getExpiryDate());
        dto.setStorageLocation(item.getStorageLocation());
        dto.setNotes(item.getNotes());

        LocalDate today = LocalDate.now();
        long daysUntil = ChronoUnit.DAYS.between(today, item.getExpiryDate());
        dto.setDaysUntilExpiry(daysUntil);
        dto.setIsExpiringSoon(daysUntil >= 0 && daysUntil <= 3);
        dto.setIsExpired(daysUntil < 0);

        return dto;
    }
}
