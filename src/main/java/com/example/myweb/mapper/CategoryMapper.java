package com.example.myweb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.example.myweb.dto.request.CategoryRequest;
import com.example.myweb.dto.respone.CategoryRespone;
import com.example.myweb.dto.respone.CategorytHomeRepone;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryRequest request);

    CategoryRespone toCategoryRespone(Category category);

    CategorytHomeRepone tCategorytHomeRepone(Category category);

    @Mapping(target = "id", ignore = true)
    void updateCategory(@MappingTarget Category category, CategoryRequest request);

    PageResponse<CategoryRespone> toPageProductRespone(Page<Category> product);

}
