package com.twitter_backend_spring_boot.twitter.common.api;

import lombok.*;
import java.time.Instant;
import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ErrorResponse {
    private boolean success;
    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
    private List<FieldErrorItem> fieldErrors;

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class FieldErrorItem {
        private String field;
        private String message;
    }
}
