package com.twitter_backend_spring_boot.twitter.api.user.dtos;

import com.twitter_backend_spring_boot.twitter.api.user.enums.FollowStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SendFollowRequest {
    @NotNull
    private Integer followerId;   // who initiates the request

    @NotNull
    private Integer followingId;  // who is being followed
}
