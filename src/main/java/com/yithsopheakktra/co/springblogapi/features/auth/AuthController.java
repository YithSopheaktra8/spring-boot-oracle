package com.yithsopheakktra.co.springblogapi.features.auth;

import com.yithsopheakktra.co.springblogapi.features.auth.dto.AuthResponse;
import com.yithsopheakktra.co.springblogapi.features.auth.dto.LoginRequest;
import com.yithsopheakktra.co.springblogapi.features.auth.dto.RefreshTokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

}
