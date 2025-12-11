package com.antigravity.ligam100.modules.auth.service;

import com.antigravity.ligam100.modules.auth.dto.AuthResponse;
import com.antigravity.ligam100.modules.auth.dto.LoginRequest;
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

    // IMPORTANTE: este es el JWTService correcto
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        // Añadimos el rol como claim
        Map<String, Object> claims = Map.of(
                "role", user.getRole().toString()
        );

        // Generamos el token usando directamente la entidad User
        var jwtToken = jwtService.generateToken(claims, user);

        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .build();
    }

}