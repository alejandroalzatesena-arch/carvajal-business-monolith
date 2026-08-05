package com.carvajal.wishlist.dto.wishlist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishlistItemRequest(
        @NotNull(message = "El id del producto es obligatorio") Long productId,
        @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1") Integer quantity) {
}
