package com.example.myweb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.myweb.dto.request.PermissionRequest;
import com.example.myweb.dto.respone.PermissionRespone;
import com.example.myweb.entity.Permission;
import com.example.myweb.mapper.PermissionMapper;
import com.example.myweb.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionRespone create(PermissionRequest request) {
        Permission permission = permissionMapper.tPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionRespone(permission);
    }

    public List<PermissionRespone> getAll() {
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionRespone).toList();
    }

    public Boolean delete(String name) {
        try {
            permissionRepository.deleteById(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
