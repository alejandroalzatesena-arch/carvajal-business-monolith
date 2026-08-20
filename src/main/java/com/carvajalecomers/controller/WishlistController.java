package com.carvajalecomers.controller;

import com.carvajalecomers.dto.WishlistItemRequest;
import com.carvajalecomers.dto.WishlistItemResponse;
import com.carvajalecomers.entity.WishlistItem;
import com.carvajalecomers.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemResponse>> listItems(
            @RequestHeader("X-User-Id") Long userId) {
        List<WishlistItem> items = wishlistService.listItems(userId);
        List<WishlistItemResponse> response = items.stream()
                .map(WishlistItemResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<WishlistItemResponse> addItem(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody WishlistItemRequest request) {
        WishlistItem item = wishlistService.addProduct(
                userId,
                request.getProductId(),
                request.getDesiredQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(WishlistItemResponse.fromEntity(item));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<WishlistItemResponse> updateItem(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody WishlistItemRequest request) {
        WishlistItem item = wishlistService.updateItem(userId, productId, request.getDesiredQuantity());
        return ResponseEntity.ok(WishlistItemResponse.fromEntity(item));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId) {
        wishlistService.removeProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
