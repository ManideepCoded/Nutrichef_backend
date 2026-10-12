package com.nutrichef.repository;

import com.nutrichef.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r WHERE " +
           "(:query IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:cuisine IS NULL OR LOWER(r.cuisineType) = LOWER(:cuisine)) AND " +
           "(:difficulty IS NULL OR LOWER(r.difficulty) = LOWER(:difficulty)) AND " +
           "(:maxTime IS NULL OR (r.prepTimeMinutes + r.cookTimeMinutes) <= :maxTime)")
    List<Recipe> searchRecipes(@Param("query") String query,
                               @Param("cuisine") String cuisine,
                               @Param("difficulty") String difficulty,
                               @Param("maxTime") Integer maxTime);
}
