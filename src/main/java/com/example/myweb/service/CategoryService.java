package com.example.myweb.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.myweb.dto.request.CategoryRequest;
import com.example.myweb.dto.respone.CategoryRespone;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.entity.Category;
import com.example.myweb.exception.AppException;
import com.example.myweb.exception.ErrorCode;
import com.example.myweb.mapper.CategoryMapper;
import com.example.myweb.repository.CategoryRepository;
import com.example.myweb.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    ProductRepository productRepository;

    public CategoryRespone creat(CategoryRequest request) {

        Category category = categoryMapper.toCategory(request);

        category.setUpdatedAt(LocalDateTime.now());

        return categoryMapper.toCategoryRespone(categoryRepository.save(category));

    }

    public CategoryRespone update(String categoryid, CategoryRequest request) {
        return categoryRepository.findById(categoryid)
                .map(existingCategory -> {
                    categoryMapper.updateCategory(existingCategory, request);
                    return categoryRepository.save(existingCategory);
                })
                .map(categoryMapper::toCategoryRespone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public Boolean delete(String categoryId) {
        try {
            // Kiểm tra xem có sản phẩm nào thuộc danh mục không
            int productCount = productRepository.countByCategoryId(categoryId);
            if (productCount > 0) {
                return false; // Không cho phép xóa nếu vẫn còn sản phẩm
            }
            // Nếu không còn sản phẩm, tiến hành xóa danh mục
            categoryRepository.deleteById(categoryId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CategoryRespone getCategory(String id) {
        return categoryMapper.toCategoryRespone(categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    public List<CategoryRespone> getAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryRespone).toList();
    }

    public PageResponse<CategoryRespone> getCategorys(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return categoryMapper.toPageProductRespone(categoryRepository.findAll(pageable));
    }

    public PageResponse<CategoryRespone> searchCategory(int page, int size, String sortBy, String direction,
            String keyword) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return categoryMapper.toPageProductRespone(categoryRepository.searchByKeyword(keyword, pageable));
    }

}
