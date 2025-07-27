package com.twitter_backend_spring_boot.twitter.api.user.serviceImpl;

import com.twitter_backend_spring_boot.twitter.api.user.dtos.*;
import com.twitter_backend_spring_boot.twitter.api.user.entities.FollowEntity;
import com.twitter_backend_spring_boot.twitter.api.user.enums.FollowStatus;
import com.twitter_backend_spring_boot.twitter.api.user.repositories.FollowRepository;
import com.twitter_backend_spring_boot.twitter.api.user.entities.UserEntity;
import com.twitter_backend_spring_boot.twitter.api.user.repositories.UserRepository;
import com.twitter_backend_spring_boot.twitter.api.user.services.FollowService;
import com.twitter_backend_spring_boot.twitter.common.exceptions.BadRequestException;
import com.twitter_backend_spring_boot.twitter.common.exceptions.ForbiddenException;
import com.twitter_backend_spring_boot.twitter.common.exceptions.ResourceAlreadyExistsException;
import com.twitter_backend_spring_boot.twitter.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FollowResponse sendFollowRequest(SendFollowRequest dto) {

        if (dto.getFollowerId().equals(dto.getFollowingId())) {
            throw new BadRequestException("You cannot follow yourself");
        }

        UserEntity follower = userRepository.findById(dto.getFollowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Follower user " + dto.getFollowerId() + " not found"));

        UserEntity following = userRepository.findById(dto.getFollowingId())
                .orElseThrow(() -> new ResourceNotFoundException("Following user " + dto.getFollowingId() + " not found"));

        // Already exists?
        followRepository.findByFollower_IdAndFollowing_Id(follower.getId(), following.getId())
                .ifPresent(existing -> {
                    switch (existing.getStatus()) {
                        case REQUESTED -> throw new ResourceAlreadyExistsException("Follow request already sent and pending");
                        case ACCEPTED -> throw new ResourceAlreadyExistsException("You already follow this user");
                        case DECLINED -> {
                            // allow re-request? You decide. Here, we allow sending again by creating a new row or reusing the same
                            throw new ResourceAlreadyExistsException("Previous request was declined");
                        }
                    }
                });

        FollowEntity entity = new FollowEntity();
        entity.setFollower(follower);
        entity.setFollowing(following);
        entity.setStatus(FollowStatus.REQUESTED);

        FollowEntity saved = followRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public FollowResponse accept(Integer followId, Integer actingUserId) {
        FollowEntity entity = followRepository.findById(followId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow request " + followId + " not found"));

        // Only the 'following' (the receiver) can accept
        ensureActingUserIsReceiver(entity, actingUserId);

        if (entity.getStatus() != FollowStatus.REQUESTED) {
            throw new BadRequestException("Only REQUESTED follow requests can be accepted");
        }

        entity.setStatus(FollowStatus.ACCEPTED);
        return toResponse(entity);
    }

    @Override
    @Transactional
    public FollowResponse decline(Integer followId, Integer actingUserId) {
        FollowEntity entity = followRepository.findById(followId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow request " + followId + " not found"));

        // Only the 'following' (the receiver) can decline
        ensureActingUserIsReceiver(entity, actingUserId);

        if (entity.getStatus() != FollowStatus.REQUESTED) {
            throw new BadRequestException("Only REQUESTED follow requests can be declined");
        }

        entity.setStatus(FollowStatus.DECLINED);
        return toResponse(entity);
    }

    private void ensureActingUserIsReceiver(FollowEntity entity, Integer actingUserId) {
        Integer receiverId = entity.getFollowing().getId();
        if (!receiverId.equals(actingUserId)) {
            throw new ForbiddenException("Only the receiving user can accept/decline the request");
        }
    }

    private FollowResponse toResponse(FollowEntity e) {
        return FollowResponse.builder()
                .id(e.getId())
                .followerId(e.getFollower().getId())
                .followingId(e.getFollowing().getId())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
