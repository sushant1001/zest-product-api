package com.zest.products.service;

import com.zest.products.dto.AuthResponse;
import com.zest.products.dto.LoginRequest;
import com.zest.products.dto.RefreshTokenRequest;
import com.zest.products.entity.User;
import com.zest.products.repository.UserRepository;
import com.zest.products.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = UUID.randomUUID().toString();


        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthResponse(accessToken, refreshToken, user.getUsername(), user.getRole());
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        User user = userRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername());

        String newRefreshToken = UUID.randomUUID().toString();

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new AuthResponse(newAccessToken, newRefreshToken, user.getUsername(), user.getRole());
    }

    public void registerUser(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
    }
}
