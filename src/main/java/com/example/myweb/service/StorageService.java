package com.example.myweb.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.myweb.exception.AppException;
import com.example.myweb.exception.ErrorCode;
import com.example.myweb.repository.ImpStorageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StorageService implements ImpStorageService {

    private final Path pathImage = Paths.get("src/main/resources/static/uploads/image");

    @Override
    public String store(MultipartFile file, Path path) {

        try {

            if (!path.getParent().equals(this.pathImage.toAbsolutePath())) {

                throw new AppException(ErrorCode.CANNOT_DIRECTORY);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return path.getFileName().toString();
        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_STORE);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.pathImage, 1)
                    .filter(path -> !path.equals(this.pathImage))
                    .map(this.pathImage::relativize);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

    }

    @Override
    public Path load(String filename) {
        return pathImage.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            }
        } catch (MalformedURLException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(pathImage.toFile());
    }

    @Override
    public void deleteProductImages(String nameImage) {
        Path productImagePath = Paths.get(pathImage.toString(), nameImage);

        try {
            if (Files.exists(productImagePath)) {
                FileSystemUtils.deleteRecursively(productImagePath);
            }
        } catch (IOException e) {
            throw new AppException(ErrorCode.IMAGE_NULL);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(pathImage);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

}
