package com.twitter_backend_spring_boot.twitter.common.api;

import lombok.*;
import java.time.Instant;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ResponseHandler<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;
    @Builder.Default
    private Instant timestamp = Instant.now();

    public static <T> ResponseHandler<T> ok(T data) {
        return ResponseHandler.<T>builder()
                .success(true)
                .status(200)
                .message("OK")
                .data(data)
                .build();
    }

    public static <T> ResponseHandler<T> created(T data) {
        return ResponseHandler.<T>builder()
                .success(true)
                .status(201)
                .message("Created")
                .data(data)
                .build();
    }
}
