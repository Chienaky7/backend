package com.example.myweb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.myweb.dto.request.UserCreatRequest;
import com.example.myweb.dto.request.UserUpdateRequest;
import com.example.myweb.dto.respone.UserRespone;
import com.example.myweb.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreatRequest request);

    UserRespone toUserRespone(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
