package com.yithsopheakktra.co.springblogapi.features.auth;

import com.yithsopheakktra.co.springblogapi.features.auth.dto.AuthResponse;
import com.yithsopheakktra.co.springblogapi.features.auth.dto.LoginRequest;
import com.yithsopheakktra.co.springblogapi.features.auth.dto.RefreshTokenRequest;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
