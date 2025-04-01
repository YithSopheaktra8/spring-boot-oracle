package com.yithsopheakktra.co.springblogapi.mapper;

import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.features.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse fromUserToUserResponse(User user);

}
