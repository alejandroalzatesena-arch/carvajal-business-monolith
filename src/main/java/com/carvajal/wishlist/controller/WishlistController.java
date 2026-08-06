package com.carvajal.wishlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carvajal.wishlist.dto.wishlist.WishlistItemRequest;
import com.carvajal.wishlist.dto.wishlist.WishlistItemResponse;
import com.carvajal.wishlist.dto.wishlist.WishlistItemUpdateRequest;
import com.carvajal.wishlist.security.SecurityUtils;
import com.carvajal.wishlist.service.WishlistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public List<WishlistItemResponse> list() {
        return wishlistService.list(SecurityUtils.getCurrentUserId());
    }

    @GetMapping("/{itemId}")
    public WishlistItemResponse getItem(@PathVariable Long itemId) {
        return wishlistService.getItemResponse(SecurityUtils.getCurrentUserId(), itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistItemResponse add(@Valid @RequestBody WishlistItemRequest request) {
        return wishlistService.add(SecurityUtils.getCurrentUserId(), request);
    }

    @PutMapping("/{itemId}")
    public WishlistItemResponse update(@PathVariable Long itemId,
                                       @Valid @RequestBody WishlistItemUpdateRequest request) {
        return wishlistService.update(SecurityUtils.getCurrentUserId(), itemId, request);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long itemId) {
        wishlistService.remove(SecurityUtils.getCurrentUserId(), itemId);
    }
}
