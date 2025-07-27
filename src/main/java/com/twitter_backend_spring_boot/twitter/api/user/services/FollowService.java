package com.twitter_backend_spring_boot.twitter.api.user.services;

import com.twitter_backend_spring_boot.twitter.api.user.dtos.*;

public interface FollowService {
    FollowResponse sendFollowRequest(SendFollowRequest dto);
    FollowResponse accept(Integer followId, Integer actingUserId);
    FollowResponse decline(Integer followId, Integer actingUserId);
}
