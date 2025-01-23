package com.example.myweb.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.myweb.dto.request.RoleRequest;
import com.example.myweb.dto.respone.RoleRespone;
import com.example.myweb.mapper.RoleMapper;
import com.example.myweb.repository.PermissionRepository;
import com.example.myweb.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleRespone create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permission));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleRespone> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public Boolean delete(String name) {
        try {
            roleRepository.deleteById(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
