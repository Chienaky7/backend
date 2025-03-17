package com.example.myweb.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.myweb.dto.request.ImageRequest;
import com.example.myweb.dto.request.ListImageRequest;
import com.example.myweb.entity.Image;
import com.example.myweb.exception.AppException;
import com.example.myweb.exception.ErrorCode;
import com.example.myweb.repository.ImageRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageService {
    private final Path pathImage = Paths.get("src/main/resources/static/uploads/image");
    ImageRepository imageRepository;
    StorageService storageService;

    public List<String> uploadImages(ListImageRequest request) {

        List<String> finalImageList = new ArrayList<>(Collections.emptyList());

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<String> uploadedImages = request.getImages().stream()
                    .map(this::saveImage)
                    .filter(Objects::nonNull)
                    .toList();

            finalImageList.addAll(uploadedImages);
        }
        return finalImageList;
    }

    public List<String> updateImages(ListImageRequest request) {

        if (request.getDelImages() != null && !request.getDelImages().isEmpty()) {
            request.getDelImages().forEach(this::delateImage);
        }

        List<String> finalImageList = new ArrayList<>(Collections.emptyList());

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<String> uploadedImages = request.getImages().stream()
                    .map(this::saveImage)
                    .filter(Objects::nonNull)
                    .toList();

            finalImageList.addAll(uploadedImages);
        }

        return finalImageList;
    }

    public Boolean deleteListImage(ListImageRequest request) {
        try {
            if (request.getDelImages() != null && !request.getDelImages().isEmpty()) {
                request.getDelImages().forEach(this::delateImage);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String updateImage(ImageRequest request) {

        if (request.getDelImages() != null && !request.getDelImages().isEmpty()) {
            delateImage(request.getDelImages());
        }

        return saveImage(request.getImages());
    }

    public String saveImage(Object image) {
        if (image instanceof MultipartFile multipartfile) {
            String originalFileName = multipartfile.getOriginalFilename();
            if (originalFileName == null) {
                throw new AppException(ErrorCode.FAILED_STORE);
            }
            String uuidFileName = UUID.randomUUID().toString()
                    + originalFileName.substring(originalFileName.lastIndexOf('.'));

            Path destinationFile = this.pathImage.resolve(
                    Paths.get(uuidFileName))
                    .normalize().toAbsolutePath();
            return storageService.store(multipartfile, destinationFile);
        } else if (image instanceof String) {
            return image.toString();
        } else {
            throw new AppException(ErrorCode.EMPTY_FILE);
        }
    }

    public Resource getImage(String nameImage) {

        return storageService.loadAsResource(nameImage);
    }

    public Boolean delateImage(String nameImage) {
        try {
            Image image = imageRepository.findByName(nameImage);
            if (image != null) {
                imageRepository.deleteById(image.getId());
                storageService.deleteProductImages(image.getName());
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
