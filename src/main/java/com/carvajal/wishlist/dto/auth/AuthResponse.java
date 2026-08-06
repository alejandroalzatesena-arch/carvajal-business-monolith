package com.carvajal.wishlist.dto.auth;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String username,
        String name,
        String email,
        String role) {
}
