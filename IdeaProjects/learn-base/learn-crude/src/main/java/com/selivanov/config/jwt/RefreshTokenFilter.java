package com.selivanov.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selivanov.dto.security.UserTokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final JwtUtil jwtUtil;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        String token = obtainToken(request);

        if (token != null) {
            try {
                // Проверка валидности RefreshToken
                UserTokenInfo userTokenInfo = jwtUtil.validateToken(token);

                // Генерируем новые AccessToken и RefreshToken
                String newAccessToken = jwtUtil.createAccessToken(userTokenInfo.username(), userTokenInfo.authorities());
                String newRefreshToken = jwtUtil.createRefreshToken(userTokenInfo.username(), userTokenInfo.authorities());

                // Отправляем новые токены в ответе
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                AuthResponse authResponse = new AuthResponse("success", userTokenInfo.username(), newAccessToken, newRefreshToken);
                MAPPER.writeValue(response.getOutputStream(), authResponse);
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                MAPPER.writeValue(response.getOutputStream(), new AuthResponse("error", null, null, null));
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String url = request.getRequestURI();
        return !"/api/refresh-token".equals(url);
    }

    private String obtainToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(JwtConfig.HEADER);
        if (authorizationHeader == null || !authorizationHeader.startsWith(JwtConfig.TOKEN_PREFIX)) {
            return null;
        }

        return authorizationHeader.replace(JwtConfig.TOKEN_PREFIX, "");
    }
}