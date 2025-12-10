package com.antigravity.ligam100.modules.auth.service;

import com.antigravity.ligam100.modules.auth.dto.AuthResponse;
import com.antigravity.ligam100.modules.auth.dto.LoginRequest;
import com.antigravity.ligam100.modules.auth.domain.User;
import com.antigravity.ligam100.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthResponse login(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
                var user = userRepository.findByUsername(request.getUsername())
                                .orElseThrow();

                // Map domain user to Security user details for token generation
                var userDetails = new org.springframework.security.core.userdetails.User(
                                user.getUsername(),
                                user.getPassword(),
                                java.util.Collections.singletonList(
                                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                                                "ROLE_" + user.getRole().name())));

                // Add role to claims
                Map<String, Object> claims = Map.of("role", user.getRole().toString());
                var jwtToken = jwtService.generateToken(claims, userDetails);

                return AuthResponse.builder()
                                .token(jwtToken)
                                .role(user.getRole().name())
                                .build();
        }

        // Registration method would go here, configured by Admin usually
}
