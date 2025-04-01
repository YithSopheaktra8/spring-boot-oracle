package com.yithsopheakktra.co.springblogapi.features.user;

import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.features.user.dto.UserResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface UserService {

    UserResponse findByUsername(String username);

    UserResponse findUserProfile(Jwt jwt);

}
