package com.softuni.project.exception;

public class UnauthorizedNotificationAccessException extends RuntimeException {
    public UnauthorizedNotificationAccessException(String message) {
        super(message);
    }
}
