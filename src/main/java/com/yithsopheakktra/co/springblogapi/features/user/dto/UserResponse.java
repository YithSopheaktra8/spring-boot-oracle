package com.yithsopheakktra.co.springblogapi.features.user.dto;

public record UserResponse(
        String uuid,
        String username,
        String email,
        String firstName,
        String lastName
) {
}
