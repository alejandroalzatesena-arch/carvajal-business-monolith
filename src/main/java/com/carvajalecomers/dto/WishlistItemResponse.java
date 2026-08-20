package com.carvajalecomers.dto;

import com.carvajalecomers.entity.WishlistItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WishlistItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer desiredQuantity;
    private LocalDateTime addedAt;

    public WishlistItemResponse() {
    }

    public WishlistItemResponse(Long id, Long productId, String productName, BigDecimal productPrice,
                                Integer desiredQuantity, LocalDateTime addedAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.desiredQuantity = desiredQuantity;
        this.addedAt = addedAt;
    }

    public static WishlistItemResponse fromEntity(WishlistItem item) {
        return new WishlistItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getDesiredQuantity(),
                item.getAddedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getDesiredQuantity() {
        return desiredQuantity;
    }

    public void setDesiredQuantity(Integer desiredQuantity) {
        this.desiredQuantity = desiredQuantity;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
