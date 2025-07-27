package com.twitter_backend_spring_boot.twitter.api.user.entities;

import com.twitter_backend_spring_boot.twitter.api.user.enums.FollowStatus;
import com.twitter_backend_spring_boot.twitter.api.user.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255, unique = true)
    private String username;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Gender gender;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "profilePicture", length = 255)
    private String profilePicture;


    @CreationTimestamp
    @Column(name = "createdAt",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    /** This user follows *others* (edges where this user is the follower). */
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<FollowEntity> following = new HashSet<>();

    /** Other users who follow *this* user (edges where this user is the followed one). */
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<FollowEntity> followers = new HashSet<>();

    // Convenience helpers (optional)
    public void follow(UserEntity other) {
        FollowEntity link = new FollowEntity(this, other, FollowStatus.REQUESTED);
        following.add(link);
        other.getFollowers().add(link);
    }

    public void unfollow(UserEntity other) {
        following.removeIf(f -> f.getFollowing().equals(other));
        other.getFollowers().removeIf(f -> f.getFollower().equals(this));
    }
}
