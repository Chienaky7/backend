package com.example.myweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.CategoryRequest;
import com.example.myweb.dto.respone.CategoryRespone;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.service.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/category")
public class CategoryController {
    CategoryService categoryService;

    @GetMapping("/{id}")
    ApiResponse<CategoryRespone> getCategory(@PathVariable String id) {

        return ApiResponse.<CategoryRespone>builder().result(categoryService.getCategory(id)).build();

    }

    @GetMapping
    ApiResponse<List<CategoryRespone>> getAll() {

        return ApiResponse.<List<CategoryRespone>>builder().result(categoryService.getAll()).build();

    }

    @PostMapping
    ApiResponse<CategoryRespone> creat(@RequestBody CategoryRequest request) {

        return ApiResponse.<CategoryRespone>builder().result(categoryService.creat(request)).build();
    }

    @PutMapping("/{id}")
    ApiResponse<CategoryRespone> update(@PathVariable String id, @RequestBody CategoryRequest request) {

        return ApiResponse.<CategoryRespone>builder().result(categoryService.update(id, request)).build();

    }

    @DeleteMapping("/{id}")
    ApiResponse<Boolean> delete(@PathVariable String id) {
        return ApiResponse.<Boolean>builder().result(categoryService.delete(id)).build();
    }

    @GetMapping("/items")
    ApiResponse<PageResponse<CategoryRespone>> getCategorys(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return ApiResponse.<PageResponse<CategoryRespone>>builder()
                .result(categoryService.getCategorys(page, size, sortBy, direction)).build();
    }

    @GetMapping("/search")
    ApiResponse<PageResponse<CategoryRespone>> getSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam String keyword) {
        return ApiResponse.<PageResponse<CategoryRespone>>builder()
                .result(categoryService.searchCategory(page, size, sortBy, direction, keyword))
                .build();
    }

}
