package com.storyart.storyservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends   RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
