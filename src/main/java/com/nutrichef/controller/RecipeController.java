package com.nutrichef.controller;

import com.nutrichef.dto.RecipeDto;
import com.nutrichef.dto.RecipeRatingDto;
import com.nutrichef.dto.RecipeRecommendationDto;
import com.nutrichef.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDto>> searchRecipes(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer maxTime) {
        List<RecipeDto> recipes = recipeService.searchRecipes(query, cuisine, difficulty, maxTime);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long id) {
        RecipeDto recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<RecipeRecommendationDto>> getRecommendations() {
        List<RecipeRecommendationDto> recommendations = recipeService.getRecommendations();
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/{id}/ratings")
    public ResponseEntity<RecipeRatingDto> rateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRatingDto ratingDto) {
        RecipeRatingDto saved = recipeService.rateRecipe(id, ratingDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
