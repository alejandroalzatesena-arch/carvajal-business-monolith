package com.carvajal.wishlist.dto.notification;

import java.time.LocalDateTime;

import com.carvajal.wishlist.entity.Product;
import com.carvajal.wishlist.entity.WishlistItem;

public record NotificationResponse(
        Long wishlistItemId,
        Long productId,
        String productName,
        Integer stock,
        LocalDateTime addedAt,
        boolean notified,
        String message) {

    public static NotificationResponse from(WishlistItem item) {
        Product product = item.getProduct();
        return new NotificationResponse(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getStock(),
                item.getAddedAt(),
                item.isNotified(),
                "El producto \"" + product.getName() + "\" no tiene stock disponible.");
    }
}
