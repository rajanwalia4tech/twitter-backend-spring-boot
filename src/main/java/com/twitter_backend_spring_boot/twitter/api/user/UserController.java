package com.twitter_backend_spring_boot.twitter.api.user;

import com.twitter_backend_spring_boot.twitter.api.user.dtos.CreateUserRequest;
import com.twitter_backend_spring_boot.twitter.api.user.dtos.FollowResponse;
import com.twitter_backend_spring_boot.twitter.api.user.dtos.SendFollowRequest;
import com.twitter_backend_spring_boot.twitter.api.user.dtos.UserResponse;
import com.twitter_backend_spring_boot.twitter.api.user.services.FollowService;
import com.twitter_backend_spring_boot.twitter.api.user.services.UserService;
import com.twitter_backend_spring_boot.twitter.common.api.ErrorResponse;
import com.twitter_backend_spring_boot.twitter.common.api.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    private final FollowService followService;

    @PostMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email or username already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @Operation(summary = "Create User while signing up")
    public ResponseEntity<ResponseHandler<UserResponse>> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse resp = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseHandler.created(resp));
    }



    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User fetched successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid path parameter",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @Operation(summary = "Get user by email")
    public ResponseEntity<ResponseHandler<UserResponse>> get(@Valid @PathVariable Integer id) {
        return ResponseEntity.ok(ResponseHandler.ok(userService.getUser(id)));
    }

    // ======= FOLLOW APIs inside UserController =======

    @PostMapping("/{targetUserId}/follow-requests")
    @Operation(summary = "Send a follow request to targetUserId")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Follow request created",
                    content = @Content(schema = @Schema(implementation = FollowResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request / self follow",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User(s) not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Request already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ResponseHandler<FollowResponse>> sendFollowRequest(
            @PathVariable Integer targetUserId,
            @Valid @RequestBody SendFollowRequest request
    ) {
        // we just forward; service will treat targetUserId as 'following'
        FollowResponse response = followService.sendFollowRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseHandler.created(response));
    }

    @PatchMapping("/{userId}/follow-requests/{followId}/accept")
    @Operation(summary = "Accept a follow request (userId must be the receiver)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accepted successfully",
                    content = @Content(schema = @Schema(implementation = FollowResponse.class))),
            @ApiResponse(responseCode = "400", description = "Not in REQUESTED state",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User is not the receiver",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Follow request not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ResponseHandler<FollowResponse>> acceptFollowRequest(
            @PathVariable Integer userId,
            @PathVariable Integer followId
    ) {
        FollowResponse response = followService.accept(followId, userId);
        return ResponseEntity.ok(ResponseHandler.ok(response));
    }

    @PatchMapping("/{userId}/follow-requests/{followId}/decline")
    @Operation(summary = "Decline a follow request (userId must be the receiver)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Declined successfully",
                    content = @Content(schema = @Schema(implementation = FollowResponse.class))),
            @ApiResponse(responseCode = "400", description = "Not in REQUESTED state",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "User is not the receiver",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Follow request not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ResponseHandler<FollowResponse>> declineFollowRequest(
            @PathVariable Integer userId,
            @PathVariable Integer followId
    ) {
        FollowResponse response = followService.decline(followId, userId);
        return ResponseEntity.ok(ResponseHandler.ok(response));
    }
}
