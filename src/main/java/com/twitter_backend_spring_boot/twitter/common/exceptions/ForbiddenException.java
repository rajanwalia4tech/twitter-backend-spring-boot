package com.twitter_backend_spring_boot.twitter.common.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) { super(message); }
}
