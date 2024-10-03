package com.ambientese.grupo5.Filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ambientese.grupo5.Services       .JWTUtil;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public AuthFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private boolean isAuthorized(String role, String requestURI) {
        List<String> managerBlockedRoutes = Arrays.asList();
        List<String> consultantBlockedRoutes = Arrays.asList("/api/auth/Employee/*", "/api/auth/User/*", "/employees");

        switch (role) {
            case "Admin":
                return true;
            case "Gestor":
                return !matchesAny(managerBlockedRoutes, requestURI);
            case "Consultor":
                return !matchesAny(consultantBlockedRoutes, requestURI);
            default:
                return false;
        }
    }

    private boolean matchesAny(List<String> routes, String requestURI) {
        for (String route : routes) {
            if (requestURI.startsWith(route.replace("*", ""))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            DecodedJWT jwt = jwtUtil.validateToken(token);
            if (jwt != null) {
                String role = jwt.getClaim("role").asString();
                String requestURI = request.getRequestURI();

                if (isAuthorized(role, requestURI)) {
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Você não tem permissão para acessar esta rota.");
                    return;
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
