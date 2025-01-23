package com.example.myweb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.myweb.dto.request.RoleRequest;
import com.example.myweb.dto.respone.RoleRespone;
import com.example.myweb.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleRespone toRoleResponse(Role role);
}
