package com.twitter_backend_spring_boot.twitter.api.user.serviceImpl;

import com.twitter_backend_spring_boot.twitter.api.user.dtos.CreateUserRequest;
import com.twitter_backend_spring_boot.twitter.api.user.dtos.UserResponse;
import com.twitter_backend_spring_boot.twitter.api.user.entities.UserEntity;
import com.twitter_backend_spring_boot.twitter.api.user.repositories.UserRepository;
import com.twitter_backend_spring_boot.twitter.api.user.services.UserService;
import com.twitter_backend_spring_boot.twitter.common.exceptions.ResourceAlreadyExistsException;
import com.twitter_backend_spring_boot.twitter.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already in use");
        }

        UserEntity entity = UserEntity.builder()
                .name(request.getName())
                .username(request.getUsername())
                .bio(request.getBio())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profilePicture(request.getProfilePicture())
                .build();

        return toResponse(userRepository.save(entity));
    }

    @Override @Transactional(readOnly = true)
    public UserResponse getUser(Integer id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
        return toResponse(entity);
    }

    private UserResponse toResponse(UserEntity e) {
        return UserResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .username(e.getUsername())
                .bio(e.getBio())
                .gender(e.getGender())
                .email(e.getEmail())
                .profilePicture(e.getProfilePicture())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
