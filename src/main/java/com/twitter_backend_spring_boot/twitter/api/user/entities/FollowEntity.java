package com.twitter_backend_spring_boot.twitter.api.user.entities;

import com.twitter_backend_spring_boot.twitter.api.user.enums.FollowStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "follows",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_follows_pair", columnNames = {"followerId", "followingId"})
        }
)
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "followerId",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_follows_follower")
    )
    private UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "followingId",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_follows_following")
    )
    private UserEntity following;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20,
            columnDefinition = "ENUM('ACCEPTED','DECLINED','REQUESTED') DEFAULT 'REQUESTED'"
    )
    private FollowStatus status;


    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public FollowEntity(UserEntity follower, UserEntity following, FollowStatus status) {
        this.follower = follower;
        this.following = following;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowEntity that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}

