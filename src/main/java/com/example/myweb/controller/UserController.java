package com.example.myweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.UserRequest;
import com.example.myweb.dto.request.UserUpdateRequest;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.dto.respone.UserRespone;
import com.example.myweb.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserService userService;

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

    @GetMapping("/getMyInfo")
    ApiResponse<UserRespone> getMyInfo() {
        return ApiResponse.<UserRespone>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Boolean> deleteUser(@PathVariable String userId) {
        return ApiResponse.<Boolean>builder()
                .result(userService.deleteUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserRespone> updateUser(@PathVariable String userId, @ModelAttribute UserUpdateRequest request) {
        return ApiResponse.<UserRespone>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @PostMapping("/admin")
    ApiResponse<UserRespone> registerAdminUser(@RequestBody UserRequest request) {
        return ApiResponse.<UserRespone>builder().result(userService.createAdminUser(request)).build();
    }

    @PutMapping("admin/{id}")
    ApiResponse<UserRespone> putAdminUser(@PathVariable String id, @RequestBody UserRequest request) {
        return ApiResponse.<UserRespone>builder().result(userService.updateAdminUser(id, request)).build();
    }

    @GetMapping("/items")
    ApiResponse<PageResponse<UserRespone>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return ApiResponse.<PageResponse<UserRespone>>builder()
                .result(userService.getUsers(page, size, sortBy, direction)).build();
    }

    @GetMapping("/search")
    ApiResponse<PageResponse<UserRespone>> getSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam String keyword) {
        return ApiResponse.<PageResponse<UserRespone>>builder()
                .result(userService.searchUser(page, size, sortBy, direction, keyword))
                .build();
    }

}
