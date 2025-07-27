package com.twitter_backend_spring_boot.twitter.api.user.services;

import com.twitter_backend_spring_boot.twitter.api.user.dtos.CreateUserRequest;
import com.twitter_backend_spring_boot.twitter.api.user.dtos.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUser(Integer id);
}
