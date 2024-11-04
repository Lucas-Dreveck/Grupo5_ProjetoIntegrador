package com.ambientese.grupo5.Services;

import com.ambientese.grupo5.Config.JWTConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.annotation.PostConstruct;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
    private Algorithm algorithm;
    private JWTVerifier verifier;
    private final JWTConfig jwtConfig;

    public JWTUtil(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    
    @PostConstruct
    public void init() {
        String secret = jwtConfig.getJwtSecret();
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    public String generateToken(String login, boolean isAdmin, String role) {
        return JWT.create()
                .withSubject(login)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}

