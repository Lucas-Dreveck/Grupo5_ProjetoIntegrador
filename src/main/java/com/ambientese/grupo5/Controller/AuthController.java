package com.ambientese.grupo5.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ambientese.grupo5.dto.UserLogin;
import com.ambientese.grupo5.services.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Auth", description = "Endpoints para autenticação")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody UserLogin user) {
        String token = authService.login(user.getLogin(), user.getPassword());
        if (token != null) {
            return ResponseEntity.ok(new TokenResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha incorretos");
        }
    }

    @PostMapping("/api/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return authService.forgotPassword(email);
    }

    @PostMapping("/api/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String recoveryCode, @RequestParam String newPassword) {
        return authService.resetPassword(email, recoveryCode, newPassword);
    }
    
    public static class TokenResponse {
        private String token;

        public TokenResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
