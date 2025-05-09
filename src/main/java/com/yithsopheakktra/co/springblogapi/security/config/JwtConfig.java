package com.yithsopheakktra.co.springblogapi.security.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    /* for access token */
    @Bean
    KeyPair accessTokenKeypair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(2048);

            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    RSAKey accessTokenKey(KeyPair accessTokenKeypair) {
        return new RSAKey.Builder((RSAPublicKey) accessTokenKeypair.getPublic())
                .privateKey(accessTokenKeypair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }


    @Primary
    @Bean
    JwtDecoder accessTokenJwtDecoder(@Qualifier("accessTokenKey") RSAKey accessTokenRSAKey) {
        try {
            return NimbusJwtDecoder
                    .withPublicKey(accessTokenRSAKey.toRSAPublicKey())
                    .build();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    JWKSource<SecurityContext> accessTokenJwkSource(@Qualifier("accessTokenKey") RSAKey accessTokenRSAKey) {
        JWKSet jwkSet = new JWKSet(accessTokenRSAKey);

        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Primary
    @Bean
    JwtEncoder accessTokenJwtEncoder(JWKSource<SecurityContext> accessTokenJwkSource) {
        return new NimbusJwtEncoder(accessTokenJwkSource);
    }

    /* for refresh token */
    @Bean
    KeyPair RrefreshTokenKeypair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(2048);

            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    RSAKey refreshTokenKey(KeyPair RrefreshTokenKeypair) {
        return new RSAKey.Builder((RSAPublicKey) RrefreshTokenKeypair.getPublic())
                .privateKey(RrefreshTokenKeypair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    JwtDecoder refreshTokenJwtDecoder( RSAKey refreshTokenKey) {
        try {
            return NimbusJwtDecoder
                    .withPublicKey(refreshTokenKey.toRSAPublicKey())
                    .build();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    JWKSource<SecurityContext> refreshTokenJwkSource( RSAKey refreshTokenKey) {
        JWKSet jwkSet = new JWKSet(refreshTokenKey);

        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    JwtEncoder refreshTokenJwtEncoder(JWKSource<SecurityContext> refreshTokenJwkSource) {
        return new NimbusJwtEncoder(refreshTokenJwkSource);
    }

}
