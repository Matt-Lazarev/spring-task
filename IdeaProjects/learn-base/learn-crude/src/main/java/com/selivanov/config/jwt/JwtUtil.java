package com.selivanov.config.jwt;

import com.selivanov.dto.security.UserTokenInfo;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public String createAccessToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return createToken(username, authorities, jwtConfig.getTokenExpirationMs());
    }

    public String createRefreshToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return createToken(username, authorities, jwtConfig.getRefreshTokenExpirationMs());
    }

    public String createToken(String username, Collection<?> authorities, int expirationMs) {
        String token = Jwts.builder()
                .issuer("spring-security-app")
                .subject(username)
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expirationMs))
                .signWith(secretKey)
                .compact();
        return JwtConfig.TOKEN_PREFIX + token;
    }

    public UserTokenInfo validateToken(String accessToken) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        String username = claims.getSubject();

        return new UserTokenInfo(username, obtainAuthorities(claims));
    }

    @SuppressWarnings("unchecked")
    private Set<? extends GrantedAuthority> obtainAuthorities(Claims claims) {
        List<Map<String, String>> authorities = claims.get("authorities", List.class);

        return authorities
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
                .collect(Collectors.toSet());
    }
}