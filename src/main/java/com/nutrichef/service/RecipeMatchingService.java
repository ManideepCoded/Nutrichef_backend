package com.nutrichef.service;

import com.nutrichef.dto.RecipeDto;
import com.nutrichef.dto.RecipeRecommendationDto;
import com.nutrichef.entity.InventoryItem;
import com.nutrichef.entity.Recipe;
import com.nutrichef.entity.RecipeIngredient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RecipeMatchingService {

    private static final Pattern CLEAN_PATTERN = Pattern.compile("[^a-zA-Z0-9 ]");

    /**
     * Normalizes ingredient names: converts to lower case, removes punctuation,
     * strips plural markers ('s', 'es'), removes culinary stop words (e.g., 'fresh', 'chopped', 'diced').
     */
    public String normalize(String input) {
        if (input == null) return "";
        String cleaned = CLEAN_PATTERN.matcher(input.toLowerCase().trim()).replaceAll(" ");
        String[] words = cleaned.split("\\s+");
        List<String> validWords = new ArrayList<>();

        Set<String> stopWords = Set.of(
                "fresh", "diced", "chopped", "sliced", "minced", "organic",
                "raw", "cooked", "dry", "dried", "ground", "extra", "virgin",
                "large", "medium", "small", "cup", "cups", "tablespoon", "tbsp",
                "teaspoon", "tsp", "gram", "grams", "oz", "ounce", "clove", "cloves"
        );

        for (String w : words) {
            if (!stopWords.contains(w) && !w.isBlank()) {
                // simple singular form
                if (w.endsWith("ies")) {
                    w = w.substring(0, w.length() - 3) + "y";
                } else if (w.endsWith("es") && w.length() > 3) {
                    w = w.substring(0, w.length() - 2);
                } else if (w.endsWith("s") && w.length() > 3 && !w.endsWith("ss")) {
                    w = w.substring(0, w.length() - 1);
                }
                validWords.add(w);
            }
        }
        return String.join(" ", validWords).trim();
    }

    /**
     * Checks if an inventory item matches a recipe ingredient based on normalized string overlap.
     */
    public boolean isIngredientMatch(String inventoryNorm, String recipeNorm) {
        if (inventoryNorm.isBlank() || recipeNorm.isBlank()) return false;
        if (inventoryNorm.equals(recipeNorm)) return true;
        if (inventoryNorm.contains(recipeNorm) || recipeNorm.contains(inventoryNorm)) return true;

        String[] invTokens = inventoryNorm.split(" ");
        String[] recTokens = recipeNorm.split(" ");
        for (String it : invTokens) {
            for (String rt : recTokens) {
                if (it.length() > 2 && rt.length() > 2 && (it.equals(rt) || it.startsWith(rt) || rt.startsWith(it))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Calculates recommendations for a list of recipes given the user's active inventory items.
     */
    public List<RecipeRecommendationDto> matchRecipes(List<Recipe> recipes, List<InventoryItem> inventoryItems, RecipeService recipeService) {
        LocalDate today = LocalDate.now();

        // Map inventory items with normalization and expiring status
        List<NormalizedInventory> userInventory = inventoryItems.stream().map(item -> {
            String norm = normalize(item.getName());
            long days = ChronoUnit.DAYS.between(today, item.getExpiryDate());
            boolean isExpiring = (days >= 0 && days <= 3);
            return new NormalizedInventory(item, norm, isExpiring);
        }).collect(Collectors.toList());

        List<RecipeRecommendationDto> results = new ArrayList<>();

        for (Recipe recipe : recipes) {
            List<RecipeIngredient> ingredients = recipe.getIngredients();
            List<RecipeIngredient> requiredIngredients = ingredients.stream()
                    .filter(i -> i.getIsOptional() == null || !i.getIsOptional())
                    .collect(Collectors.toList());

            int totalRequired = requiredIngredients.isEmpty() ? Math.max(1, ingredients.size()) : requiredIngredients.size();
            List<String> matching = new ArrayList<>();
            List<String> missing = new ArrayList<>();
            Set<String> expiringIngredientsUsed = new LinkedHashSet<>();

            List<RecipeIngredient> targetList = requiredIngredients.isEmpty() ? ingredients : requiredIngredients;

            for (RecipeIngredient req : targetList) {
                String reqNorm = normalize(req.getIngredientName());
                boolean found = false;

                for (NormalizedInventory inv : userInventory) {
                    if (isIngredientMatch(inv.normalizedName, reqNorm)) {
                        found = true;
                        matching.add(req.getIngredientName());
                        if (inv.isExpiringSoon) {
                            expiringIngredientsUsed.add(inv.item.getName() + " (expires in " +
                                    ChronoUnit.DAYS.between(today, inv.item.getExpiryDate()) + "d)");
                        }
                        break;
                    }
                }

                if (!found) {
                    missing.add(req.getIngredientName());
                }
            }

            double matchPercentage = totalRequired > 0
                    ? Math.round(((double) matching.size() / totalRequired) * 1000.0) / 10.0
                    : 100.0;

            RecipeRecommendationDto recDto = new RecipeRecommendationDto();
            recDto.setRecipe(recipeService.mapToDto(recipe));
            recDto.setMatchPercentage(matchPercentage);
            recDto.setAvailableCount(matching.size());
            recDto.setTotalRequiredCount(totalRequired);
            recDto.setMatchingIngredients(matching);
            recDto.setMissingIngredients(missing);
            recDto.setUsesExpiringIngredients(!expiringIngredientsUsed.isEmpty());
            recDto.setExpiringIngredientNames(new ArrayList<>(expiringIngredientsUsed));

            results.add(recDto);
        }

        // Sort: First by usesExpiringIngredients (descending), then by matchPercentage (descending)
        results.sort((a, b) -> {
            if (a.getUsesExpiringIngredients() != b.getUsesExpiringIngredients()) {
                return Boolean.compare(b.getUsesExpiringIngredients(), a.getUsesExpiringIngredients());
            }
            return Double.compare(b.getMatchPercentage(), a.getMatchPercentage());
        });

        return results;
    }

    private static class NormalizedInventory {
        final InventoryItem item;
        final String normalizedName;
        final boolean isExpiringSoon;

        NormalizedInventory(InventoryItem item, String normalizedName, boolean isExpiringSoon) {
            this.item = item;
            this.normalizedName = normalizedName;
            this.isExpiringSoon = isExpiringSoon;
        }
    }
}
