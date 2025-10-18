package com.santiagomac.auth.infrastructure.entry_points;

import com.santiagomac.auth.application.dto.AuthRequest;
import com.santiagomac.auth.application.dto.AuthResponse;
import com.santiagomac.auth.application.dto.RegisterRequest;
import com.santiagomac.auth.application.dto.RegisterResponse;
import com.santiagomac.auth.application.usecases.LoginUseCase;
import com.santiagomac.auth.application.usecases.RegisterUseCase;
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

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        var registerUser = this.registerUseCase.signup(request);

        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(this.loginUseCase.authenticate(authRequest));
    }
}
