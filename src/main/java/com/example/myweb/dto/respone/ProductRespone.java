package com.example.myweb.dto.respone;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.myweb.entity.ProductVariant;

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
public class ProductRespone {
    String id;
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
    List<ImageRepone> images;
    CategoryRespone category;
    List<ProductVariant> variants;
    LocalDateTime createdAt;
}
