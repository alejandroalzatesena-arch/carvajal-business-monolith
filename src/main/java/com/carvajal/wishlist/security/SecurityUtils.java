package com.carvajal.wishlist.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import com.carvajal.wishlist.entity.User;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        return user;
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
