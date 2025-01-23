package com.example.myweb.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myweb.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Set<Role> findByName(String name);
}
