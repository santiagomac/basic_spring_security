package com.santiagomac.template.auth.infrastructure.entry_points;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import com.santiagomac.template.auth.application.dto.AuthRequest;
import com.santiagomac.template.auth.application.dto.AuthResponse;
import com.santiagomac.template.auth.application.dto.RegisterRequest;
import com.santiagomac.template.auth.application.dto.RegisterResponse;
import com.santiagomac.template.auth.application.usecases.AuthUseCase;
import com.santiagomac.template.auth.application.usecases.LoginUseCase;
import com.santiagomac.template.auth.application.usecases.RegisterUseCase;
import com.santiagomac.template.auth.domain.model.exceptions.InvalidRefreshToken;
import com.santiagomac.template.auth.infrastructure.config.AuthProperties;
import com.santiagomac.template.auth.infrastructure.config.SecurityConfiguration;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@ConditionalOnAuthEnabled
@RequestMapping(SecurityConfiguration.AUTH_PATH)
@RequiredArgsConstructor
public class SecurityController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final AuthUseCase authUseCase;
    private final AuthProperties properties;

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUseCase.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody AuthRequest request) {
        return withCookies(loginUseCase.authenticate(request));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Void> refreshToken(
            @CookieValue(name = SecurityConfiguration.REFRESH_COOKIE, required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new InvalidRefreshToken("Missing refresh token");
        }
        return withCookies(authUseCase.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = SecurityConfiguration.REFRESH_COOKIE, required = false) String refreshToken) {
        if (refreshToken != null) {
            authUseCase.logout(refreshToken);
        }
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE,
                        cookie(SecurityConfiguration.ACCESS_COOKIE, "", "/", Duration.ZERO).toString())
                .header(HttpHeaders.SET_COOKIE,
                        cookie(SecurityConfiguration.REFRESH_COOKIE, "", SecurityConfiguration.AUTH_PATH, Duration.ZERO).toString())
                .build();
    }

    private ResponseEntity<Void> withCookies(AuthResponse session) {
        var jwt = properties.jwt();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        cookie(SecurityConfiguration.ACCESS_COOKIE, session.accessToken(), "/", jwt.accessTtl()).toString())
                .header(HttpHeaders.SET_COOKIE,
                        cookie(SecurityConfiguration.REFRESH_COOKIE, session.refreshToken(), SecurityConfiguration.AUTH_PATH, jwt.refreshTtl()).toString())
                .build();
    }

    private ResponseCookie cookie(String name, String value, String path, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(properties.cookie().secure())
                .sameSite(properties.cookie().sameSite())
                .path(path)
                .maxAge(maxAge)
                .build();
    }
}
