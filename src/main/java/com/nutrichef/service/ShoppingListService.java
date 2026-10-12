package com.nutrichef.service;

import com.nutrichef.dto.ShoppingListItemDto;
import com.nutrichef.entity.Recipe;
import com.nutrichef.entity.ShoppingListItem;
import com.nutrichef.entity.User;
import com.nutrichef.exception.ResourceNotFoundException;
import com.nutrichef.repository.RecipeRepository;
import com.nutrichef.repository.ShoppingListItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private final ShoppingListItemRepository shoppingRepository;
    private final RecipeRepository recipeRepository;
    private final UserService userService;

    public ShoppingListService(ShoppingListItemRepository shoppingRepository,
                               RecipeRepository recipeRepository,
                               UserService userService) {
        this.shoppingRepository = shoppingRepository;
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ShoppingListItemDto> getUserShoppingList() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return shoppingRepository.findByUserIdOrderByIsCompletedAscCreatedAtDesc(currentUser.getId())
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public ShoppingListItemDto addShoppingItem(ShoppingListItemDto dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();

        ShoppingListItem item = new ShoppingListItem();
        item.setUser(currentUser);
        item.setName(dto.getName().trim());
        item.setCategory(dto.getCategory() != null ? dto.getCategory().toUpperCase().trim() : "GROCERY");
        item.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 1.0);
        item.setUnit(dto.getUnit() != null ? dto.getUnit().trim() : "pcs");
        item.setIsCompleted(false);

        if (dto.getSourceRecipeId() != null) {
            recipeRepository.findById(dto.getSourceRecipeId()).ifPresent(item::setSourceRecipe);
        }

        ShoppingListItem saved = shoppingRepository.save(item);
        return mapToDto(saved);
    }

    @Transactional
    public ShoppingListItemDto updateShoppingItem(Long id, ShoppingListItemDto dto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        ShoppingListItem item = shoppingRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shopping list item " + id + " not found or unauthorized"));

        if (dto.getName() != null) item.setName(dto.getName().trim());
        if (dto.getQuantity() != null) item.setQuantity(dto.getQuantity());
        if (dto.getUnit() != null) item.setUnit(dto.getUnit().trim());
        if (dto.getIsCompleted() != null) item.setIsCompleted(dto.getIsCompleted());
        if (dto.getCategory() != null) item.setCategory(dto.getCategory().toUpperCase().trim());

        ShoppingListItem saved = shoppingRepository.save(item);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteShoppingItem(Long id) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        ShoppingListItem item = shoppingRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shopping list item " + id + " not found or unauthorized"));
        shoppingRepository.delete(item);
    }

    private ShoppingListItemDto mapToDto(ShoppingListItem item) {
        ShoppingListItemDto dto = new ShoppingListItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setCategory(item.getCategory());
        dto.setQuantity(item.getQuantity());
        dto.setUnit(item.getUnit());
        dto.setIsCompleted(item.getIsCompleted());
        if (item.getSourceRecipe() != null) {
            dto.setSourceRecipeId(item.getSourceRecipe().getId());
            dto.setSourceRecipeTitle(item.getSourceRecipe().getTitle());
        }
        dto.setCreatedAt(item.getCreatedAt());
        return dto;
    }
}
