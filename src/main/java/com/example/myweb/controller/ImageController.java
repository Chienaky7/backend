package com.example.myweb.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myweb.dto.request.ApiResponse;
import com.example.myweb.dto.request.ImageRequest;
import com.example.myweb.dto.request.ListImageRequest;
import com.example.myweb.service.ImageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/image")
public class ImageController {

    ImageService imageService;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> image(@PathVariable String filename) {

        Resource file = imageService.getImage(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // hoặc MediaType.IMAGE_PNG nếu ảnh là PNG
                .body(file);
    }

    @DeleteMapping("/{nameImage}")
    ApiResponse<Boolean> getProduct(@PathVariable String nameImage) {
        return ApiResponse.<Boolean>builder().result(imageService.delateImage(nameImage)).build();
    }

    @PostMapping("/list")
    ApiResponse<List<String>> uploadProductImages(@ModelAttribute ListImageRequest request) {
        return ApiResponse.<List<String>>builder().result(imageService.uploadImages(request)).build();
    }

    @DeleteMapping("/list")
    ApiResponse<Boolean> deleteProductImages(@ModelAttribute ListImageRequest request) {
        return ApiResponse.<Boolean>builder().result(imageService.deleteListImage(request)).build();
    }

    @PostMapping
    ApiResponse<String> uploadImages(@ModelAttribute ImageRequest request) {
        return ApiResponse.<String>builder().result(imageService.updateImage(request)).build();
    }

    @PutMapping("/list")
    ApiResponse<List<String>> updateImages(@ModelAttribute ListImageRequest request) {
        return ApiResponse.<List<String>>builder().result(imageService.updateImages(request)).build();
    }
}
