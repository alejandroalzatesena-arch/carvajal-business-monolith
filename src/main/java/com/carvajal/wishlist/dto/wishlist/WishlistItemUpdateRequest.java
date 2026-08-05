package com.carvajal.wishlist.dto.wishlist;

import jakarta.validation.constraints.Min;

public record WishlistItemUpdateRequest(
        @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1") Integer quantity) {
}
