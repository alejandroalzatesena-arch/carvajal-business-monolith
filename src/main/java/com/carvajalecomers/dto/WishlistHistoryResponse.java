package com.carvajalecomers.dto;

import com.carvajalecomers.entity.WishlistAction;
import com.carvajalecomers.entity.WishlistHistory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WishlistHistoryResponse {

    private Long id;
    private Long productId;
    private String productNameSnapshot;
    private BigDecimal productPriceSnapshot;
    private WishlistAction action;
    private LocalDateTime actionAt;

    public WishlistHistoryResponse() {
    }

    public WishlistHistoryResponse(Long id, Long productId, String productNameSnapshot,
                                   BigDecimal productPriceSnapshot, WishlistAction action,
                                   LocalDateTime actionAt) {
        this.id = id;
        this.productId = productId;
        this.productNameSnapshot = productNameSnapshot;
        this.productPriceSnapshot = productPriceSnapshot;
        this.action = action;
        this.actionAt = actionAt;
    }

    public static WishlistHistoryResponse fromEntity(WishlistHistory history) {
        return new WishlistHistoryResponse(
                history.getId(),
                history.getProduct().getId(),
                history.getProductNameSnapshot(),
                history.getProductPriceSnapshot(),
                history.getAction(),
                history.getActionAt()
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

    public String getProductNameSnapshot() {
        return productNameSnapshot;
    }

    public void setProductNameSnapshot(String productNameSnapshot) {
        this.productNameSnapshot = productNameSnapshot;
    }

    public BigDecimal getProductPriceSnapshot() {
        return productPriceSnapshot;
    }

    public void setProductPriceSnapshot(BigDecimal productPriceSnapshot) {
        this.productPriceSnapshot = productPriceSnapshot;
    }

    public WishlistAction getAction() {
        return action;
    }

    public void setAction(WishlistAction action) {
        this.action = action;
    }

    public LocalDateTime getActionAt() {
        return actionAt;
    }

    public void setActionAt(LocalDateTime actionAt) {
        this.actionAt = actionAt;
    }
}
