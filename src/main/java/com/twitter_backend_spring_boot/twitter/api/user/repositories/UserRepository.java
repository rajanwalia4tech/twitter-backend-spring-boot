package com.twitter_backend_spring_boot.twitter.api.user.repositories;

import com.twitter_backend_spring_boot.twitter.api.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
