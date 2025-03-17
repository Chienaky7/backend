package com.example.myweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> hanlingAppException(AppException exception) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.builder()
                        .code(exception.getErrorCode().getCode())
                        .message(exception.getMessage())
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

    @ExceptionHandler(value = NullPointerException.class)
    ResponseEntity<ApiResponse<AppException>> hanlingAppException(NullPointerException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.<AppException>builder()
                        .code(9999)
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // ✅ Trả về HTTP 403
    ResponseEntity<ApiResponse<AppException>> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.<AppException>builder()
                        .code(403)
                        .message("Bạn không có quyền truy cập vào tài nguyên này!")
                        .build());
    }
}
