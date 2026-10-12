package com.nutrichef.repository;

import com.nutrichef.entity.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {

    List<MealLog> findByUserIdOrderByLogDateDescCreatedAtDesc(Long userId);

    List<MealLog> findByUserIdAndLogDateOrderByCreatedAtAsc(Long userId, LocalDate logDate);

    @Query("SELECT COALESCE(SUM(m.calories), 0) FROM MealLog m WHERE m.user.id = :userId AND m.logDate = :logDate")
    Integer getTotalCaloriesByDate(@Param("userId") Long userId, @Param("logDate") LocalDate logDate);

    @Query("SELECT COALESCE(SUM(m.proteinGrams), 0.0) FROM MealLog m WHERE m.user.id = :userId AND m.logDate = :logDate")
    Double getTotalProteinByDate(@Param("userId") Long userId, @Param("logDate") LocalDate logDate);

    @Query("SELECT COALESCE(SUM(m.carbsGrams), 0.0) FROM MealLog m WHERE m.user.id = :userId AND m.logDate = :logDate")
    Double getTotalCarbsByDate(@Param("userId") Long userId, @Param("logDate") LocalDate logDate);

    @Query("SELECT COALESCE(SUM(m.fatGrams), 0.0) FROM MealLog m WHERE m.user.id = :userId AND m.logDate = :logDate")
    Double getTotalFatByDate(@Param("userId") Long userId, @Param("logDate") LocalDate logDate);
}
