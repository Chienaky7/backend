package com.example.myweb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.myweb.dto.request.ProductVariantRequest;
import com.example.myweb.entity.ProductVariant;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariant toProductVariant(ProductVariantRequest request);

    @Mapping(target = "product", ignore = true)
    void updateProductVariant(@MappingTarget ProductVariant productVariant,
            ProductVariantRequest productVariantRequest);
}
