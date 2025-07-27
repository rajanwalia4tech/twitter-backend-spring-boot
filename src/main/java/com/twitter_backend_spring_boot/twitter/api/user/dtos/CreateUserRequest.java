package com.twitter_backend_spring_boot.twitter.api.user.dtos;

import com.twitter_backend_spring_boot.twitter.api.user.enums.Gender;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[A-Za-z0-9_.]+$", message = "username may contain letters, numbers, underscores and dots only")
    private String username;

    @Size(max = 1000)
    private String bio;

    @NotNull
    private Gender gender;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 8, max = 64, message = "password must be between 8 and 64 characters")
    private String password;

    @URL(message = "profilePicture must be a valid URL")
    @Size(max = 255)
    private String profilePicture;
}
