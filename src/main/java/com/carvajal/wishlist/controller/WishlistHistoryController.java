package com.carvajal.wishlist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carvajal.wishlist.dto.history.WishlistHistoryResponse;
import com.carvajal.wishlist.service.WishlistHistoryService;

@RestController
@RequestMapping("/api/users/{userId}/wishlist/history")
public class WishlistHistoryController {

    private final WishlistHistoryService wishlistHistoryService;

    public WishlistHistoryController(WishlistHistoryService wishlistHistoryService) {
        this.wishlistHistoryService = wishlistHistoryService;
    }

    @GetMapping
    public List<WishlistHistoryResponse> list(@PathVariable Long userId) {
        return wishlistHistoryService.listByUser(userId);
    }
}
