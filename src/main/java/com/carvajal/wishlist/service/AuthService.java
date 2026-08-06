package com.carvajal.wishlist.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carvajal.wishlist.dto.auth.AuthResponse;
import com.carvajal.wishlist.dto.auth.LoginRequest;
import com.carvajal.wishlist.dto.auth.RegisterRequest;
import com.carvajal.wishlist.entity.User;
import com.carvajal.wishlist.entity.enums.Role;
import com.carvajal.wishlist.exception.DuplicateResourceException;
import com.carvajal.wishlist.repository.UserRepository;
import com.carvajal.wishlist.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("El nombre de usuario ya está registrado");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("El correo ya está registrado");
        }

        User user = new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                Role.USER,
                true);
        User saved = userRepository.save(user);
        return buildAuthResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + request.username()));
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        return new AuthResponse(
                jwtService.generateToken(user),
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().name());
    }
}
