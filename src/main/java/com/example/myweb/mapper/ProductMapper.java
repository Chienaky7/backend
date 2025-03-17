package com.example.myweb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.example.myweb.dto.request.ProductRequest;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.dto.respone.ProductCategoryRepone;
import com.example.myweb.dto.respone.ProductRespone;
import com.example.myweb.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "variants", ignore = true)
    Product toProduct(ProductRequest request);

    ProductCategoryRepone toProductRespone(Product product);

    @Mapping(target = "category", source = "category")
    ProductRespone tProductRespone(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "variants", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest request);

    PageResponse<ProductRespone> toListProductRespone(Page<Product> product);

}
