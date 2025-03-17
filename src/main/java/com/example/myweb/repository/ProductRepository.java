package com.example.myweb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.myweb.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    int countByCategoryId(String categoryId);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<Product> findByCategoryId(String categoryId);

}
