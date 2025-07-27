package com.twitter_backend_spring_boot.twitter.common.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) { super(message); }
}
