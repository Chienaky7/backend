package com.example.myweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.UserCreatRequest;
import com.example.myweb.dto.request.UserUpdateRequest;
import com.example.myweb.dto.respone.UserRespone;
import com.example.myweb.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserRespone> createUser(@RequestBody @Valid UserCreatRequest request) {
        return new ApiResponse<>(1000, "success", userService.creatUser(request));
    }

    @GetMapping()
    ApiResponse<List<UserRespone>> getAllUsers() {
        return ApiResponse.<List<UserRespone>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserRespone> getUsers(@PathVariable String userId) {
        return ApiResponse.<UserRespone>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Boolean> deleteUser(@PathVariable String userId) {
        return ApiResponse.<Boolean>builder()
                .result(userService.deleteUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserRespone> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserRespone>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

}
