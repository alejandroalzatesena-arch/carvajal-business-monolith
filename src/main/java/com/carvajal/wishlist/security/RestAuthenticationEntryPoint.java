
package com.carvajal.wishlist.security;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write("""
                    {"timestamp":"%s","status":401,"error":"Unauthorized","message":"Credenciales inválidas o token expirado"}"""
                    .formatted(LocalDateTime.now()));
        } catch (java.io.IOException ex) {
            // respuesta ya en curso, no se puede hacer más
        }
    }
}
