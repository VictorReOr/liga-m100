package com.antigravity.ligam100.modules.auth.service;

import com.antigravity.ligam100.config.JwtService;
import com.antigravity.ligam100.modules.auth.domain.User;
import com.antigravity.ligam100.modules.auth.dto.AuthDto;
import com.antigravity.ligam100.modules.auth.repository.UserRepository;
import com.antigravity.ligam100.modules.province.domain.Cpa;
import com.antigravity.ligam100.modules.province.repository.CpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final CpaRepository cpaRepository; // Needed to link CPA
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthDto.AuthenticationResponse register(AuthDto.RegisterRequest request) {
        Cpa cpa = null;
        if (request.getCpaId() != null) {
            cpa = cpaRepository.findById(request.getCpaId()).orElse(null);
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .nombreCompleto(request.getNombreCompleto())
                .cpa(cpa)
                .validated(true) // Configurar validación manual si se requiere
                .build();
        
        userRepository.save(user); // Guardar primero para obtener ID si fuera necesario
        
        var jwtToken = jwtService.generateToken(user);
        
        return AuthDto.AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .cpaId(user.getCpa() != null ? user.getCpa().getId() : null)
                .build();
    }

    public AuthDto.AuthenticationResponse authenticate(AuthDto.AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        
        return AuthDto.AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .cpaId(user.getCpa() != null ? user.getCpa().getId() : null)
                .build();
    }
}
