package com.nutrichef.repository;

import com.nutrichef.entity.RecipeRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long> {
    
    Optional<RecipeRating> findByRecipeIdAndUserId(Long recipeId, Long userId);
    
    List<RecipeRating> findByRecipeId(Long recipeId);

    @Query("SELECT AVG(r.rating) FROM RecipeRating r WHERE r.recipe.id = :recipeId")
    Double getAverageRatingForRecipe(@Param("recipeId") Long recipeId);

    @Query("SELECT COUNT(r) FROM RecipeRating r WHERE r.recipe.id = :recipeId")
    Long getRatingCountForRecipe(@Param("recipeId") Long recipeId);
}
