package com.example.myweb.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.dto.respone.ProductRespone;
import com.example.myweb.entity.Product;

@Mapper(componentModel = "spring")
public interface PageMapper {

    PageResponse<ProductRespone> toResponse(Page<Product> page);

}
