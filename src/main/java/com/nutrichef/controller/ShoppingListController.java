package com.nutrichef.controller;

import com.nutrichef.dto.ShoppingListItemDto;
import com.nutrichef.service.ShoppingListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingListItemDto>> getShoppingList() {
        List<ShoppingListItemDto> list = shoppingListService.getUserShoppingList();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ShoppingListItemDto> addShoppingItem(@Valid @RequestBody ShoppingListItemDto dto) {
        ShoppingListItemDto created = shoppingListService.addShoppingItem(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListItemDto> updateShoppingItem(
            @PathVariable Long id,
            @RequestBody ShoppingListItemDto dto) {
        ShoppingListItemDto updated = shoppingListService.updateShoppingItem(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingItem(@PathVariable Long id) {
        shoppingListService.deleteShoppingItem(id);
        return ResponseEntity.noContent().build();
    }
}
