package com.example.myweb.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    String description;
    BigDecimal price;
    BigDecimal salePrice;
    int stock;
    String sku;
    String barcode;
    BigDecimal weight;
    double length;
    double width;
    double height;
    String title;
    String descriptionSeo;
    String keywords;
    String status;
    Boolean featured;
    Boolean taxable;
    String publishDate;
    CategoryRequest category;
    List<String> images;
    List<ProductVariantRequest> variants;
}
