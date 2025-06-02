package com.selivanov.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selivanov.dto.error.ErrorResponse;
import com.selivanov.model.auth.AuthRequest;
import com.selivanov.model.auth.AuthResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Override
    @SneakyThrows //login
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // Проверка метода запроса
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Only POST method is allowed");
        }
        // Чтение данных аутентификации из тела запроса
        AuthRequest authRequest = MAPPER.readValue(request.getInputStream(), AuthRequest.class);

        // Создание объекта UsernamePasswordAuthenticationToken с данными пользователя
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authRequest.username(), authRequest.password()
        );
        // Аутентификация пользователя
        return authManager.authenticate(authentication);
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult){
        String accessToken = jwtUtil.createAccessToken(authResult.getName(), authResult.getAuthorities());
        String refreshToken = jwtUtil.createRefreshToken(authResult.getName(), authResult.getAuthorities());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        MAPPER.writeValue(
                response.getOutputStream(),
                new AuthResponse("success", authResult.getName(), accessToken, refreshToken)
        );
    }

    @SneakyThrows
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), failed.getMessage());
        MAPPER.writeValue(response.getOutputStream(), errorResponse);
    }
}