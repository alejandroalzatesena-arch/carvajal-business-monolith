package com.carvajalecomers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "wishlist_items",
        uniqueConstraints = @UniqueConstraint(name = "uq_wishlist_product", columnNames = {"wishlist_id", "product_id"})
)
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "desired_quantity", nullable = false)
    private Integer desiredQuantity = 1;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    public WishlistItem() {
    }

    public WishlistItem(Wishlist wishlist, Product product, Integer desiredQuantity) {
        this.wishlist = wishlist;
        this.product = product;
        this.desiredQuantity = desiredQuantity;
    }

    @PrePersist
    protected void onCreate() {
        this.addedAt = LocalDateTime.now();
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
