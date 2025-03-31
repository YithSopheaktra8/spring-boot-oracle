package com.yithsopheakktra.co.springblogapi.features.auth;

import com.yithsopheakktra.co.springblogapi.features.auth.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtEncoder accessTokenJwtEncoder;

    @Override
    public AuthResponse login(String username, String password) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        daoAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);

        // create JWT token here
        Instant now = Instant.now();

        JwtClaimsSet accessTokenJwtClaimsSet = JwtClaimsSet.builder()
                .id(username)
                .issuedAt(now)
                .subject("Access Resource")
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .build();

        JwtEncoderParameters accessTokenParameters = JwtEncoderParameters
                .from(accessTokenJwtClaimsSet);

        String accessToken = accessTokenJwtEncoder.encode(accessTokenParameters).getTokenValue();

        return new AuthResponse(accessToken);
    }
}
