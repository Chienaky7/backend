package com.example.myweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.AuthenticationRequest;
import com.example.myweb.dto.request.IntrospectRequest;
import com.example.myweb.dto.request.UserCreatRequest;
import com.example.myweb.dto.respone.AuthenticationRespone;
import com.example.myweb.service.AuthenticationService;
import com.example.myweb.service.UserService;
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
    UserService userService;

    @PostMapping("/login")
    ApiResponse<AuthenticationRespone> login(@RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationRespone>builder().result(authenticationService.login(request)).build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        authenticationService.logout(request.getToken());
        return new ApiResponse<>(1000, "logout", "true");
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationRespone> refreshToken(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {

        return ApiResponse.<AuthenticationRespone>builder().result(authenticationService.refreshToken(request)).build();
    }

    @PostMapping("/register")
    ApiResponse<AuthenticationRespone> register(@RequestBody UserCreatRequest request) {
        return ApiResponse.<AuthenticationRespone>builder().result(userService.creatUser(request)).build();
    }

}
