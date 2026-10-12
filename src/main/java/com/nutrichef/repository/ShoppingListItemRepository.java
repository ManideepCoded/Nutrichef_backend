package com.nutrichef.repository;

import com.nutrichef.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {

    List<ShoppingListItem> findByUserIdOrderByIsCompletedAscCreatedAtDesc(Long userId);

    Optional<ShoppingListItem> findByIdAndUserId(Long id, Long userId);

    long countByUserIdAndIsCompletedFalse(Long userId);
}
