package com.selivanov.config.jwt;

import com.selivanov.client.AuthClient;
import com.selivanov.model.auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private final AuthClient authClient;

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        AuthResponse tokenResponse = authClient.getToken();

        if (tokenResponse != null && tokenResponse.accessToken() != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, tokenResponse.accessToken());
        }

        return execution.execute(request, body);
    }
}