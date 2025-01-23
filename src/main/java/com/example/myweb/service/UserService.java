package com.example.myweb.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.myweb.dto.request.UserCreatRequest;
import com.example.myweb.dto.request.UserUpdateRequest;
import com.example.myweb.dto.respone.UserRespone;
import com.example.myweb.entity.User;
import com.example.myweb.exception.AppException;
import com.example.myweb.exception.ErrorCode;
import com.example.myweb.mapper.UserMapper;
import com.example.myweb.repository.RoleRepository;
import com.example.myweb.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    RoleRepository roleRepository;

    @PostAuthorize("returnObject.username = authentication.name")
    public UserRespone getMyInfo() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userMapper.toUserRespone(
                userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserRespone creatUser(UserCreatRequest request) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(request.getUsername())))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRoles(roleRepository.findByName("USER"));
        return userMapper.toUserRespone(userRepository.save(user));

    }

    @PreAuthorize("hasAuthority('33333')")
    public List<UserRespone> getAllUser() {
        return userRepository.findAll().stream().map(userMapper::toUserRespone).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserRespone getUser(String userId) {
        return userMapper.toUserRespone(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public Boolean deleteUser(String userId) {

        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        userRepository.deleteById(userId);
        return true;
    }

    public UserRespone updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));

        userRepository.save(user);

        return userMapper.toUserRespone(user);
    }
}
