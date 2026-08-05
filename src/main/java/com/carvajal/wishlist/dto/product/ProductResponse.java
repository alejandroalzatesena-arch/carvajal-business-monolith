package com.carvajal.wishlist.dto.product;

import java.math.BigDecimal;

import com.carvajal.wishlist.entity.Product;

public record ProductResponse(
        Long id,
        String code,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String category,
        String imageUrl) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrl());
    }
}
