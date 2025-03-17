package com.example.myweb.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.myweb.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Page<Category> findAll(Pageable pageable);

    @Query("SELECT p FROM Category p WHERE p.name LIKE %:keyword%")
    Page<Category> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<Category> findById(String id);
}
