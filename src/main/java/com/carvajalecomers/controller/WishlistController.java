package com.carvajalecomers.controller;

import com.carvajalecomers.dto.WishlistHistoryResponse;
import com.carvajalecomers.dto.WishlistItemRequest;
import com.carvajalecomers.dto.WishlistItemResponse;
import com.carvajalecomers.entity.WishlistHistory;
import com.carvajalecomers.entity.WishlistItem;
import com.carvajalecomers.security.CustomUserDetails;
import com.carvajalecomers.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<WishlistItemResponse>> listItems() {
        Long userId = getCurrentUserId();
        List<WishlistItem> items = wishlistService.listItems(userId);
        List<WishlistItemResponse> response = items.stream()
                .map(WishlistItemResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<WishlistItemResponse> addItem(
            @Valid @RequestBody WishlistItemRequest request) {
        Long userId = getCurrentUserId();
        WishlistItem item = wishlistService.addProduct(
                userId,
                request.getProductId(),
                request.getDesiredQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(WishlistItemResponse.fromEntity(item));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<WishlistItemResponse> updateItem(
            @PathVariable Long productId,
            @Valid @RequestBody WishlistItemRequest request) {
        Long userId = getCurrentUserId();
        WishlistItem item = wishlistService.updateItem(userId, productId, request.getDesiredQuantity());
        return ResponseEntity.ok(WishlistItemResponse.fromEntity(item));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        wishlistService.removeProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<WishlistHistoryResponse>> getHistory() {
        Long userId = getCurrentUserId();
        List<WishlistHistory> history = wishlistService.getHistory(userId);
        List<WishlistHistoryResponse> response = history.stream()
                .map(WishlistHistoryResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }
}
