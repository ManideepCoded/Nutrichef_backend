package com.nutrichef.repository;

import com.nutrichef.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    
    List<InventoryItem> findByUserIdOrderByExpiryDateAsc(Long userId);

    Optional<InventoryItem> findByIdAndUserId(Long id, Long userId);

    List<InventoryItem> findByUserIdAndCategoryOrderByExpiryDateAsc(Long userId, String category);

    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND i.expiryDate BETWEEN :today AND :thresholdDate ORDER BY i.expiryDate ASC")
    List<InventoryItem> findExpiringSoon(@Param("userId") Long userId, @Param("today") LocalDate today, @Param("thresholdDate") LocalDate thresholdDate);

    @Query("SELECT i FROM InventoryItem i WHERE i.user.id = :userId AND i.expiryDate < :today ORDER BY i.expiryDate ASC")
    List<InventoryItem> findExpired(@Param("userId") Long userId, @Param("today") LocalDate today);

    long countByUserId(Long userId);

    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.user.id = :userId AND i.expiryDate BETWEEN :today AND :thresholdDate")
    long countExpiringSoon(@Param("userId") Long userId, @Param("today") LocalDate today, @Param("thresholdDate") LocalDate thresholdDate);
}
