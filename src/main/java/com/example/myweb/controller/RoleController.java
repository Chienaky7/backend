package com.example.myweb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.RoleRequest;
import com.example.myweb.dto.respone.RoleRespone;
import com.example.myweb.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/role")
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleRespone> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleRespone>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping()
    ApiResponse<List<RoleRespone>> getAll() {
        return ApiResponse.<List<RoleRespone>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Boolean> delete(@PathVariable String name) {
        return ApiResponse.<Boolean>builder()
                .result(roleService.delete(name))
                .build();
    }
}
