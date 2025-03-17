package com.example.myweb.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.myweb.dto.request.ProductRequest;
import com.example.myweb.dto.request.ProductVariantRequest;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.dto.respone.ProductCategoryRepone;
import com.example.myweb.dto.respone.ProductRespone;
import com.example.myweb.entity.Category;
import com.example.myweb.entity.Product;
import com.example.myweb.entity.Image;
import com.example.myweb.entity.ProductVariant;
import com.example.myweb.exception.AppException;
import com.example.myweb.exception.ErrorCode;
import com.example.myweb.mapper.PageMapper;
import com.example.myweb.mapper.ProductMapper;
import com.example.myweb.mapper.ProductVariantMapper;
import com.example.myweb.repository.CategoryRepository;
import com.example.myweb.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    ProductVariantMapper productVariantMapper;
    PageMapper pageMapper;

    public ProductRespone creatProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        Category category = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        if (!request.getImages().isEmpty() && request.getImages() != null) {
            product.setImages(new HashSet<>(request.getImages().stream()
                    .map(img -> Image.builder().product(product).name(img).build()).toList()));
        }

        if (!request.getVariants().isEmpty() && request.getVariants() != null) {
            product.setVariants(new HashSet<>(request.getVariants().stream()
                    .map(variant -> ProductVariant.builder().type(variant.getType()).value(variant.getValue())
                            .stock(variant.getStock()).product(product).sku(variant.getSku()).price(variant.getPrice())
                            .build())
                    .toList()));
        }

        return productMapper.tProductRespone(productRepository.save(product));

    }

    public ProductRespone saveProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productMapper.updateProduct(product, request);

        // Cập nhật danh mục
        Category category = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);

        // Cập nhật variants
        updateProductVariants(product, request.getVariants());

        // Cập nhật images
        updateProductImages(product, request.getImages());

        return productMapper.tProductRespone(productRepository.save(product));
    }

    /**
     * Cập nhật danh sách variants của sản phẩm.
     */
    private void updateProductVariants(Product product, List<ProductVariantRequest> variantRequests) {
        if (variantRequests == null || variantRequests.isEmpty()) {
            return;
        }

        // Tạo map lookup nhanh hơn
        Map<String, ProductVariant> existingVariantMap = Optional.ofNullable(product.getVariants())
                .orElse(Collections.emptySet()).stream()
                .collect(Collectors.toMap(ProductVariant::getId, v -> v));

        Set<ProductVariant> updatedVariants = new HashSet<>();

        for (ProductVariantRequest request : variantRequests) {
            ProductVariant variant = existingVariantMap.get(request.getId());

            if (variant != null) {
                productVariantMapper.updateProductVariant(variant, request);
            } else {
                variant = productVariantMapper.toProductVariant(request);
                variant.setProduct(product);
            }
            updatedVariants.add(variant);
        }

        // Xóa các variants không còn sử dụng
        product.getVariants().removeIf(v -> !updatedVariants.contains(v));
        product.getVariants().addAll(updatedVariants);
    }

    /**
     * Cập nhật danh sách images của sản phẩm.
     */
    private void updateProductImages(Product product, List<String> imageNames) {
        if (imageNames == null || imageNames.isEmpty()) {
            return;
        }

        // Tạo map lookup nhanh hơn
        Map<String, Image> existingImageMap = Optional.ofNullable(product.getImages())
                .orElse(Collections.emptySet()) // Nếu null, trả về Set rỗng
                .stream()
                .collect(Collectors.toMap(Image::getName, img -> img));

        Set<Image> updatedImages = new HashSet<>();

        for (String name : imageNames) {
            Image image = existingImageMap.get(name);
            if (image == null) {
                image = Image.builder().name(name).product(product).build();
            }
            updatedImages.add(image);
        }

        // Xóa ảnh không còn tồn tại
        product.getImages().removeIf(img -> !updatedImages.contains(img));
        product.getImages().addAll(updatedImages);
    }

    public Boolean delete(String productId) {
        try {
            productRepository.deleteById(productId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<ProductCategoryRepone> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(productMapper::toProductRespone)
                .collect(Collectors.toList());
    }

    public List<ProductRespone> getAll() {
        return productRepository.findAll().stream()
                .map(productMapper::tProductRespone)
                .toList();
    }

    public ProductRespone getProduct(String id) {
        return productMapper.tProductRespone(productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    public PageResponse<ProductRespone> getPageProducts(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return pageMapper.toResponse(productRepository.findAll(pageable));
    }

    public PageResponse<ProductRespone> searchProduct(int page, int size, String sortBy, String direction,
            String keyword) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productMapper.toListProductRespone(productRepository.searchByKeyword(keyword, pageable));
    }

}
