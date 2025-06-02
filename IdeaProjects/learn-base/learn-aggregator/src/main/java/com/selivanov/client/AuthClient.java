package com.selivanov.client;

import com.selivanov.model.auth.AuthRequest;
import com.selivanov.model.auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AuthClient {
    private final RestTemplate firstRestTemplatePlain;

    @Value("${clients.first.username}")
    private String username;

    // симм. шифрование: gjargh83g8 -> decode("gjargh83g8", key) -> 12345
    // passwordEncoder.encode(rawPassword) -> gjargh83g8
    @Value("${clients.first.password}")
    private String password;

    public AuthResponse getToken() {
        return firstRestTemplatePlain.postForObject(
                 "/login",
                new AuthRequest(username, password),
                AuthResponse.class
        );
    }
}
