package com.yithsopheakktra.co.springblogapi.features.user;


import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.features.user.dto.UserResponse;
import com.yithsopheakktra.co.springblogapi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponse findByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        return userMapper.fromUserToUserResponse(user);
    }

    @Override
    public UserResponse findUserProfile(Jwt jwt) {

        if(jwt == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized access, token not found"
            );
        }

        User user = userRepository.findByUuid(jwt.getClaimAsString("jti"))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return userMapper.fromUserToUserResponse(user);

    }
}
