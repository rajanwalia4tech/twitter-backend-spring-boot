package com.twitter_backend_spring_boot.twitter.api.user.dtos;

import com.twitter_backend_spring_boot.twitter.api.user.enums.FollowStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FollowResponse{
    private Integer id;
    private Integer followerId;
    private Integer followingId;
    private FollowStatus status;
    private LocalDateTime createdAt;
}
