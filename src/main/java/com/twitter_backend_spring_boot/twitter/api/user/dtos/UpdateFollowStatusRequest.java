package com.twitter_backend_spring_boot.twitter.api.user.dtos;

import com.twitter_backend_spring_boot.twitter.api.user.enums.FollowStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateFollowStatusRequest {
    @NotNull
    private FollowStatus status; // ACCEPTED or DECLINED
    @NotNull
    private Integer actingUserId; // (recommended) the user who is accepting/declining (should be the 'following' user)
}
