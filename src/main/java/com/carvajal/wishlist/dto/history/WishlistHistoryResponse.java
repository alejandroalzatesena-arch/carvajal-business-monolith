package com.carvajal.wishlist.dto.history;

import java.time.LocalDateTime;

import com.carvajal.wishlist.entity.WishlistHistory;
import com.carvajal.wishlist.entity.enums.ActionType;

public record WishlistHistoryResponse(
        Long id,
        Long productId,
        String productName,
        ActionType action,
        String detail,
        LocalDateTime createdAt) {

    public static WishlistHistoryResponse from(WishlistHistory history) {
        return new WishlistHistoryResponse(
                history.getId(),
                history.getProduct().getId(),
                history.getProduct().getName(),
                history.getAction(),
                history.getDetail(),
                history.getCreatedAt());
    }
}
