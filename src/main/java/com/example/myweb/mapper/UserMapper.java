package com.example.myweb.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.example.myweb.dto.request.UserCreatRequest;
import com.example.myweb.dto.request.UserRequest;
import com.example.myweb.dto.request.UserUpdateRequest;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.dto.respone.UserRespone;
import com.example.myweb.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreatRequest request);

    UserRespone toUserRespone(User user);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    User toAdminUser(UserRequest request);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    void updateAdminUser(@MappingTarget User user, UserRequest request);

    PageResponse<UserRespone> toPageUserRespone(Page<User> userPage);

    List<UserRespone> toUserResponeList(List<User> users);

}
