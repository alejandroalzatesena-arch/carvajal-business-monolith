package com.carvajalecomers.repository;

import com.carvajalecomers.entity.WishlistHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistHistoryRepository extends JpaRepository<WishlistHistory, Long> {

    List<WishlistHistory> findByWishlistIdOrderByActionAtDesc(Long wishlistId);
}
