package com.carvajal.wishlist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carvajal.wishlist.dto.history.WishlistHistoryResponse;
import com.carvajal.wishlist.repository.WishlistHistoryRepository;

@Service
public class WishlistHistoryService {

    private final WishlistHistoryRepository wishlistHistoryRepository;

    public WishlistHistoryService(WishlistHistoryRepository wishlistHistoryRepository) {
        this.wishlistHistoryRepository = wishlistHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<WishlistHistoryResponse> listByUser(Long userId) {
        return wishlistHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(WishlistHistoryResponse::from)
                .toList();
    }
}
