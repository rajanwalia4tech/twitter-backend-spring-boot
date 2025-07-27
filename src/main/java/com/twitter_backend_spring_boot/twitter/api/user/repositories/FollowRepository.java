package com.twitter_backend_spring_boot.twitter.api.user.repositories;

import com.twitter_backend_spring_boot.twitter.api.user.entities.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {
    boolean existsByFollower_IdAndFollowing_Id(Integer followerId, Integer followingId);

    Optional<FollowEntity> findByFollower_IdAndFollowing_Id(Integer followerId, Integer followingId);
}