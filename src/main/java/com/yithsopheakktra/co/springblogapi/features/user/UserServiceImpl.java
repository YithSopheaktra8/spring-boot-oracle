package com.yithsopheakktra.co.springblogapi.features.user;


import com.yithsopheakktra.co.springblogapi.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public User findByUsername(String username) {

        User user = userRepository.findByUsername(username).orElse(null);

        return user;
    }
}
