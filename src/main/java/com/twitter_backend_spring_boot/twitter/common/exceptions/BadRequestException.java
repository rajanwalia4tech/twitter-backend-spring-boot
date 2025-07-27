package com.twitter_backend_spring_boot.twitter.common.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
