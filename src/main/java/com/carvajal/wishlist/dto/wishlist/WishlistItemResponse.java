package com.carvajal.wishlist.dto.wishlist;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.carvajal.wishlist.entity.Product;
import com.carvajal.wishlist.entity.WishlistItem;

public record WishlistItemResponse(
        Long id,
        Long productId,
        String productCode,
        String productName,
        String category,
        BigDecimal price,
        Integer stock,
        Integer quantity,
        LocalDateTime addedAt,
        boolean outOfStock) {

    public static WishlistItemResponse from(WishlistItem item) {
        Product product = item.getProduct();
        return new WishlistItemResponse(
                item.getId(),
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                item.getQuantity(),
                item.getAddedAt(),
                product.getStock() <= 0);
    }
}
