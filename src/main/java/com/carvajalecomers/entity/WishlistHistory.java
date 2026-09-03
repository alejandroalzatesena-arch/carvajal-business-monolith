package com.carvajalecomers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist_history")
public class WishlistHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private WishlistAction action;

    @Column(name = "product_name_snapshot", nullable = false, length = 150)
    private String productNameSnapshot;

    @Column(name = "product_price_snapshot", nullable = false, precision = 12, scale = 2)
    private BigDecimal productPriceSnapshot;

    @Column(name = "action_at", nullable = false)
    private LocalDateTime actionAt;

    public WishlistHistory() {
    }

    public WishlistHistory(Wishlist wishlist, Product product, WishlistAction action,
                            String productNameSnapshot, BigDecimal productPriceSnapshot) {
        this.wishlist = wishlist;
        this.product = product;
        this.action = action;
        this.productNameSnapshot = productNameSnapshot;
        this.productPriceSnapshot = productPriceSnapshot;
    }

    @PrePersist
    protected void onCreate() {
        this.actionAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public WishlistAction getAction() {
        return action;
    }

    public void setAction(WishlistAction action) {
        this.action = action;
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

    public LocalDateTime getActionAt() {
        return actionAt;
    }

    public void setActionAt(LocalDateTime actionAt) {
        this.actionAt = actionAt;
    }
}
