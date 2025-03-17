package com.example.myweb.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.ProductRequest;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.dto.respone.ProductCategoryRepone;
import com.example.myweb.dto.respone.ProductRespone;
import com.example.myweb.service.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/product")
public class ProductController {

        ProductService productService;

        @PostMapping
        ApiResponse<ProductRespone> create(@RequestBody ProductRequest request) {
                return ApiResponse.<ProductRespone>builder()
                                .result(productService.creatProduct(request))
                                .build();
        }

        @PutMapping("/{id}")
        ApiResponse<ProductRespone> update(@PathVariable String id, @RequestBody ProductRequest request) {
                return ApiResponse.<ProductRespone>builder()
                                .result(productService.saveProduct(id, request))
                                .build();
        }

        @GetMapping("/{productId}")
        ApiResponse<ProductRespone> getProduct(@PathVariable String productId) {
                return ApiResponse.<ProductRespone>builder().result(productService.getProduct(productId)).build();
        }

        @DeleteMapping("/{productId}")
        ApiResponse<Boolean> deleteProduct(@PathVariable String productId) {
                return ApiResponse.<Boolean>builder().result(productService.delete(productId)).build();
        }

        @GetMapping("/category/{categoryId}")
        ApiResponse<List<ProductCategoryRepone>> getProductsByCategory(@PathVariable String categoryId) {

                return ApiResponse.<List<ProductCategoryRepone>>builder()
                                .result(productService.getProductsByCategoryId(categoryId)).build();
        }

        @GetMapping("/items")
        ApiResponse<PageResponse<ProductRespone>> getPageProducts(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String direction) {
                return ApiResponse.<PageResponse<ProductRespone>>builder()
                                .result(productService.getPageProducts(page, size, sortBy, direction)).build();
        }

        @GetMapping("/search")
        ApiResponse<PageResponse<ProductRespone>> getSearch(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "desc") String direction,
                        @RequestParam String keyword) {
                return ApiResponse.<PageResponse<ProductRespone>>builder()
                                .result(productService.searchProduct(page, size, sortBy, direction, keyword))
                                .build();
        }

}
