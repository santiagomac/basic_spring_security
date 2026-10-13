package com.santiagomac.template.auth.infrastructure.config;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.util.WebUtils;

/** Active when app.auth.enabled=true: JWT resource server reading the token from the Authorization header or a cookie. */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnAuthEnabled
public class SecurityConfiguration {

    public static final String AUTH_PATH = "/api/v1/auth";
    public static final String ACCESS_COOKIE = "access_token";
    public static final String REFRESH_COOKIE = "refresh_token";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_PATH + "/**", "/actuator/health/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth
                        .bearerTokenResolver(bearerTokenResolver())
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    /** Bearer header first, access_token cookie second. Skipped on /auth/** so a stale token cannot block login. */
    private BearerTokenResolver bearerTokenResolver() {
        var headerResolver = new DefaultBearerTokenResolver();
        return request -> {
            if (request.getServletPath().startsWith(AUTH_PATH)) {
                return null;
            }
            String header = headerResolver.resolve(request);
            if (header != null) {
                return header;
            }
            Cookie cookie = WebUtils.getCookie(request, ACCESS_COOKIE);
            return cookie == null ? null : cookie.getValue();
        };
    }

    /** Maps the roles claim to authorities and rejects refresh tokens presented as access tokens. */
    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        var authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles");
        authorities.setAuthorityPrefix("");

        var delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(authorities);
        return jwt -> {
            if (!"access".equals(jwt.getClaimAsString("token_type"))) {
                throw new InvalidBearerTokenException("Not an access token");
            }
            return delegate.convert(jwt);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
