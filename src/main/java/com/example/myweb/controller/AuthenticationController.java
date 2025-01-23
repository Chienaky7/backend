package com.example.myweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.AuthenticationRequest;
import com.example.myweb.dto.request.IntrospectRequest;
import com.example.myweb.dto.request.LogoutRequest;
import com.example.myweb.dto.respone.AuthenticationRespone;
import com.example.myweb.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationRespone> login(@RequestBody AuthenticationRequest request) {
        return new ApiResponse<>(1000, "login",
                authenticationService.login(request));
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authenticationService.logout(request);
        return new ApiResponse<>(1000, "logout", "true");
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationRespone> refreshToken(@RequestBody IntrospectRequest request)
            throws JOSEException, ParseException {

        return ApiResponse.<AuthenticationRespone>builder().code(1000).message("refresh")
                .result(authenticationService.refreshToken(request)).build();
    }

}
