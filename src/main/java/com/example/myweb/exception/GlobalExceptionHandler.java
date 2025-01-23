package com.example.myweb.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.myweb.dto.request.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<String>> hanlingNullPointerException(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .code(404)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<ErrorCode>> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ErrorCode errorCode;
        try {
            errorCode = ErrorCode.valueOf(exception.getFieldError().getDefaultMessage());
        } catch (Exception e) {
            errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        }

        return ResponseEntity.status(errorCode.getStatusCode()).body(ApiResponse.<ErrorCode>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());

    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<AppException>> hanlingAppException(AppException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.<AppException>builder()
                        .code(exception.getErrorCode().getCode())
                        .message(exception.getErrorCode().getMessage())
                        .build());
    }

    @ExceptionHandler(value = JwtException.class)
    ResponseEntity<ApiResponse<AppException>> hanlingAppException(JwtException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.<AppException>builder()
                        .code(9999)
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<String>> storageFileNotFoundException(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.<String>builder()
                        .code(9999)
                        .message(exception.getMessage())
                        .build());
    }
}
