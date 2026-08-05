package com.carvajal.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carvajal.wishlist.entity.WishlistHistory;

public interface WishlistHistoryRepository extends JpaRepository<WishlistHistory, Long> {

    List<WishlistHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
