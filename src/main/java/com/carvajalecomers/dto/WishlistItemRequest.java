package com.carvajalecomers.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishlistItemRequest {

    @NotNull(message = "El id del producto es obligatorio")
    private Long productId;

    @Min(value = 1, message = "La cantidad deseada debe ser al menos 1")
    private Integer desiredQuantity;

    public WishlistItemRequest() {
    }

    public WishlistItemRequest(Long productId, Integer desiredQuantity) {
        this.productId = productId;
        this.desiredQuantity = desiredQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getDesiredQuantity() {
        return desiredQuantity;
    }

    public void setDesiredQuantity(Integer desiredQuantity) {
        this.desiredQuantity = desiredQuantity;
    }
}
