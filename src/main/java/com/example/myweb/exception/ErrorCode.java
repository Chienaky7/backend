package com.example.myweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    CANNOT_DIRECTORY(1010, "Cannot store file outside current directory.", HttpStatus.BAD_REQUEST),
    FAILED_STORE(1011, "Failed to store file.", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(10012, "Category not found.", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(10012, "Pruduct not found.", HttpStatus.BAD_REQUEST),
    EMPTY_FILE(1009, "Failed to store empty file.", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1010, "Token invalid", HttpStatus.UNAUTHORIZED),
    IMAGE_NULL(1010, "Image invalid", HttpStatus.UNAUTHORIZED);

    int code;
    String message;
    HttpStatusCode statusCode;

}
