package com.yithsopheakktra.co.springblogapi.features.auth;

import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.features.auth.dto.AuthResponse;
import com.yithsopheakktra.co.springblogapi.features.auth.dto.LoginRequest;
import com.yithsopheakktra.co.springblogapi.features.auth.dto.RefreshTokenRequest;
import com.yithsopheakktra.co.springblogapi.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtEncoder accessTokenJwtEncoder;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtEncoder refreshTokenJwtEncoder;
    private final JwtDecoder refreshTokenJwtDecoder;
    private final UserRepository userRepository;
    @Qualifier("applicationTaskExecutor")
    private final AsyncTaskExecutor asyncTaskExecutor;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

        Authentication authenticate = daoAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);


        asyncTaskExecutor.execute(() -> {
            // Log the authentication event
            log.info("User {} authenticated successfully", loginRequest.username());
        });

        // create JWT token here
        Instant now = Instant.now();

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        JwtClaimsSet accessTokenJwtClaimsSet = JwtClaimsSet.builder()
                .id(user.getUuid())
                .issuedAt(now)
                .subject("Access Resource")
                .claim("scope", authenticate.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", "")) // Remove ROLE_
                        .collect(Collectors.joining(" "))) // Store as space-separated string
                .expiresAt(now.plus(2, ChronoUnit.MINUTES))
                .build();

        JwtEncoderParameters accessTokenParameters = JwtEncoderParameters
                .from(accessTokenJwtClaimsSet);

        JwtClaimsSet refreshTokenJwtClaimsSet = JwtClaimsSet.builder()
                .id(loginRequest.username())
                .issuedAt(now)
                .subject("Refresh Resource")
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .build();

        JwtEncoderParameters refreshTokenParameters = JwtEncoderParameters
                .from(refreshTokenJwtClaimsSet);

        String accessToken = accessTokenJwtEncoder.encode(accessTokenParameters).getTokenValue();
        String refreshToken = refreshTokenJwtEncoder.encode(refreshTokenParameters).getTokenValue();
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();

        try {
            // Decode the refresh token manually
            Jwt jwt = refreshTokenJwtDecoder.decode(refreshToken);

            // Extract username from refresh token
            String username = jwt.getId();

            // Generate a new access token
            Instant now = Instant.now();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "FAILED TO REFRESH TOKEN"
                    ));

            JwtClaimsSet accessTokenJwtClaimsSet = JwtClaimsSet.builder()
                    .id(user.getUuid())
                    .issuedAt(now)
                    .subject("Access Resource")
                    .claim("scope", user.getRole()) // Fetch user roles from DB
                    .expiresAt(now.plus(1, ChronoUnit.HOURS))
                    .build();

            JwtEncoderParameters accessTokenParameters = JwtEncoderParameters.from(accessTokenJwtClaimsSet);
            String newAccessToken = accessTokenJwtEncoder.encode(accessTokenParameters).getTokenValue();

            JwtClaimsSet refreshTokenJwtClaimsSet = JwtClaimsSet.builder()
                    .id(username)
                    .issuedAt(now)
                    .subject("refresh Resource")
                    .expiresAt(now.plus(1, ChronoUnit.DAYS))
                    .build();

            JwtEncoderParameters refreshTokenParameters = JwtEncoderParameters
                    .from(refreshTokenJwtClaimsSet);

            refreshToken = refreshTokenJwtEncoder.encode(refreshTokenParameters).getTokenValue();

            return new AuthResponse(newAccessToken, refreshToken); // Return new access token

        } catch (JwtException e) {
            throw new RuntimeException("Invalid refresh token", e);
        }
    }


}
