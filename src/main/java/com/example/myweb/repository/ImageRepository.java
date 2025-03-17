package com.example.myweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myweb.entity.Image;

public interface ImageRepository extends JpaRepository<Image, String> {
    Boolean deleteByName(String name);

    Image findByName(String name);
}
