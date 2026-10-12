package com.nutrichef.controller;

import com.nutrichef.dto.InventoryItemDto;
import com.nutrichef.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getInventory(@RequestParam(required = false) String category) {
        List<InventoryItemDto> items = inventoryService.getUserInventory(category);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<InventoryItemDto> addInventoryItem(@Valid @RequestBody InventoryItemDto dto) {
        InventoryItemDto created = inventoryService.addInventoryItem(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDto> getInventoryItem(@PathVariable Long id) {
        InventoryItemDto item = inventoryService.getInventoryItem(id);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDto> updateInventoryItem(@PathVariable Long id, @Valid @RequestBody InventoryItemDto dto) {
        InventoryItemDto updated = inventoryService.updateInventoryItem(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        inventoryService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<InventoryItemDto>> getExpiringItems() {
        List<InventoryItemDto> items = inventoryService.getExpiringSoonItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<InventoryItemDto>> getExpiredItems() {
        List<InventoryItemDto> items = inventoryService.getExpiredItems();
        return ResponseEntity.ok(items);
    }
}
