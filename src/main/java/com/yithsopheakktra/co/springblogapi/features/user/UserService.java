package com.yithsopheakktra.co.springblogapi.features.user;

import com.yithsopheakktra.co.springblogapi.domain.User;

import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

}
