package com.twitter_backend_spring_boot.twitter.exceptions;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
    private final HttpStatus status;

    public HttpException(HttpStatus code, String message) {
        super(message);
        this.status = code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
