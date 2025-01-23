package com.example.myweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.PermissionRequest;
import com.example.myweb.dto.respone.PermissionRespone;
import com.example.myweb.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permission")
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionRespone> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionRespone>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping()
    ApiResponse<List<PermissionRespone>> getAll() {
        return ApiResponse.<List<PermissionRespone>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Boolean> deleteUser(@PathVariable String name) {
        return ApiResponse.<Boolean>builder()
                .result(permissionService.delete(name))
                .build();
    }

}
