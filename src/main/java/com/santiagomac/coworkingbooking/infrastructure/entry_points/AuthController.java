package com.santiagomac.coworkingbooking.infrastructure.entry_points;

import com.santiagomac.coworkingbooking.application.JwtService;
import com.santiagomac.coworkingbooking.application.dto.LoginResponse;
import com.santiagomac.coworkingbooking.application.dto.LoginUserDto;
import com.santiagomac.coworkingbooking.application.dto.RegisterUserDto;
import com.santiagomac.coworkingbooking.domain.service.AuthenticationService;
import com.santiagomac.coworkingbooking.infrastructure.driven_adapter.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registerUser = authenticationService.signUp(registerUserDto);

        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwt = jwtService.generateToken(authenticatedUser);
        return ResponseEntity.ok(LoginResponse.builder()
                .token(jwt)
                .expiresIn(jwtService.getExpirationTime())
                .build());
    }
}
