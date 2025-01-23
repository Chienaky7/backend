package com.example.myweb.mapper;

import org.mapstruct.Mapper;

import com.example.myweb.dto.request.PermissionRequest;
import com.example.myweb.dto.respone.PermissionRespone;
import com.example.myweb.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission tPermission(PermissionRequest request);

    PermissionRespone toPermissionRespone(Permission permission);

}
