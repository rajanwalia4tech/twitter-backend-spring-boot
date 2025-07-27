package com.twitter_backend_spring_boot.twitter.api.user.dtos;

import com.twitter_backend_spring_boot.twitter.api.user.enums.Gender;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String name;
    private String username;
    private String bio;
    private Gender gender;
    private String email;
    private String profilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
