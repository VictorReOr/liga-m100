package com.antigravity.ligam100.modules.auth.dto;

import com.antigravity.ligam100.modules.auth.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequest {
        private String username;
        private String password;
        private String nombreCompleto;
        private UserRole role;
        private Long cpaId; // Opcional, solo para RESPONSABLE DE CPA
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticationRequest {
        private String username;
        private String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticationResponse {
        private String token;
        private Long userId;
        private String username;
        private UserRole role;
        private Long cpaId;
    }
}
