package com.nutrichef.service;

import com.nutrichef.dto.DashboardSummaryDto;
import com.nutrichef.dto.NutritionSummaryDto;
import com.nutrichef.dto.RecipeRecommendationDto;
import com.nutrichef.entity.User;
import com.nutrichef.repository.InventoryItemRepository;
import com.nutrichef.repository.ShoppingListItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final InventoryItemRepository inventoryRepository;
    private final ShoppingListItemRepository shoppingRepository;
    private final NutritionService nutritionService;
    private final RecipeService recipeService;
    private final UserService userService;

    public DashboardService(InventoryItemRepository inventoryRepository,
                            ShoppingListItemRepository shoppingRepository,
                            NutritionService nutritionService,
                            RecipeService recipeService,
                            UserService userService) {
        this.inventoryRepository = inventoryRepository;
        this.shoppingRepository = shoppingRepository;
        this.nutritionService = nutritionService;
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(3);

        long totalInventory = inventoryRepository.countByUserId(currentUser.getId());
        long expiringSoon = inventoryRepository.countExpiringSoon(currentUser.getId(), today, threshold);
        long expired = inventoryRepository.findExpired(currentUser.getId(), today).size();
        long pendingShopping = shoppingRepository.countByUserIdAndIsCompletedFalse(currentUser.getId());

        NutritionSummaryDto nutrition = nutritionService.getNutritionSummary(today);
        List<RecipeRecommendationDto> recommendations = recipeService.getRecommendations();

        DashboardSummaryDto summary = new DashboardSummaryDto();
        summary.setTotalInventoryItems(totalInventory);
        summary.setExpiringSoonItems(expiringSoon);
        summary.setExpiredItems(expired);
        summary.setPendingShoppingItems(pendingShopping);

        summary.setTodayCaloriesConsumed(nutrition.getTotalCalories());
        summary.setDailyCalorieTarget(nutrition.getTargetCalories());
        summary.setTodayProteinConsumed(nutrition.getTotalProtein());
        summary.setTodayCarbsConsumed(nutrition.getTotalCarbs());
        summary.setTodayFatConsumed(nutrition.getTotalFat());

        summary.setTopRecommendations(recommendations.stream().limit(3).collect(Collectors.toList()));

        return summary;
    }
}
