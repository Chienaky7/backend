package com.example.myweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myweb.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

}
