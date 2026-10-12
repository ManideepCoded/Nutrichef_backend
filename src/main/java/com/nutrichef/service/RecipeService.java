package com.nutrichef.service;

import com.nutrichef.dto.RecipeDto;
import com.nutrichef.dto.RecipeRatingDto;
import com.nutrichef.dto.RecipeRecommendationDto;
import com.nutrichef.entity.*;
import com.nutrichef.exception.BadRequestException;
import com.nutrichef.exception.ResourceNotFoundException;
import com.nutrichef.repository.InventoryItemRepository;
import com.nutrichef.repository.RecipeRatingRepository;
import com.nutrichef.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeRatingRepository ratingRepository;
    private final InventoryItemRepository inventoryRepository;
    private final UserService userService;
    private final RecipeMatchingService matchingService;

    public RecipeService(RecipeRepository recipeRepository,
                         RecipeRatingRepository ratingRepository,
                         InventoryItemRepository inventoryRepository,
                         UserService userService,
                         RecipeMatchingService matchingService) {
        this.recipeRepository = recipeRepository;
        this.ratingRepository = ratingRepository;
        this.inventoryRepository = inventoryRepository;
        this.userService = userService;
        this.matchingService = matchingService;
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> searchRecipes(String query, String cuisine, String difficulty, Integer maxTime) {
        String cleanQuery = (query != null && !query.isBlank()) ? query.trim() : null;
        String cleanCuisine = (cuisine != null && !cuisine.isBlank()) ? cuisine.trim() : null;
        String cleanDifficulty = (difficulty != null && !difficulty.isBlank()) ? difficulty.trim() : null;

        List<Recipe> recipes = recipeRepository.searchRecipes(cleanQuery, cleanCuisine, cleanDifficulty, maxTime);
        return recipes.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecipeDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe with ID " + id + " not found"));
        return mapToDto(recipe);
    }

    @Transactional(readOnly = true)
    public List<RecipeRecommendationDto> getRecommendations() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        List<Recipe> allRecipes = recipeRepository.findAll();
        List<InventoryItem> userInventory = inventoryRepository.findByUserIdOrderByExpiryDateAsc(currentUser.getId());

        return matchingService.matchRecipes(allRecipes, userInventory, this);
    }

    @Transactional
    public RecipeRatingDto rateRecipe(Long recipeId, RecipeRatingDto ratingDto) {
        if (ratingDto.getRating() == null || ratingDto.getRating() < 1 || ratingDto.getRating() > 5) {
            throw new BadRequestException("Rating must be an integer between 1 and 5");
        }

        User currentUser = userService.getCurrentAuthenticatedUser();
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe with ID " + recipeId + " not found"));

        RecipeRating rating = ratingRepository.findByRecipeIdAndUserId(recipeId, currentUser.getId())
                .orElse(new RecipeRating(recipe, currentUser, ratingDto.getRating(), ratingDto.getComment()));

        rating.setRating(ratingDto.getRating());
        rating.setComment(ratingDto.getComment());

        RecipeRating saved = ratingRepository.save(rating);

        RecipeRatingDto response = new RecipeRatingDto();
        response.setId(saved.getId());
        response.setRecipeId(recipeId);
        response.setUserId(currentUser.getId());
        response.setUserName(currentUser.getFullName());
        response.setRating(saved.getRating());
        response.setComment(saved.getComment());
        response.setCreatedAt(saved.getCreatedAt());

        return response;
    }

    public RecipeDto mapToDto(Recipe recipe) {
        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setInstructions(recipe.getInstructions());
        dto.setPrepTimeMinutes(recipe.getPrepTimeMinutes());
        dto.setCookTimeMinutes(recipe.getCookTimeMinutes());
        dto.setServings(recipe.getServings());
        dto.setDifficulty(recipe.getDifficulty());
        dto.setCuisineType(recipe.getCuisineType());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setCaloriesPerServing(recipe.getCaloriesPerServing());
        dto.setProteinGrams(recipe.getProteinGrams());
        dto.setCarbsGrams(recipe.getCarbsGrams());
        dto.setFatGrams(recipe.getFatGrams());

        Double avgRating = ratingRepository.getAverageRatingForRecipe(recipe.getId());
        Long count = ratingRepository.getRatingCountForRecipe(recipe.getId());
        dto.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 4.5);
        dto.setRatingCount(count != null ? count : 0L);

        if (recipe.getIngredients() != null) {
            dto.setIngredients(recipe.getIngredients().stream().map(i ->
                    new RecipeDto.IngredientDto(
                            i.getId(),
                            i.getIngredientName(),
                            i.getNormalizedName(),
                            i.getQuantity(),
                            i.getUnit(),
                            i.getIsOptional()
                    )
            ).collect(Collectors.toList()));
        }

        return dto;
    }
}
