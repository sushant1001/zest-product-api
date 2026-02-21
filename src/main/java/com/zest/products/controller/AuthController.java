package com.zest.products.controller;

import com.zest.products.dto.*;
import com.zest.products.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Login and token endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Get new access token using refresh token")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed", response));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user (for testing)")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody LoginRequest request) {

        authService.registerUser(request.getUsername(), request.getPassword(), "ROLE_USER");
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", null));
    }
}
