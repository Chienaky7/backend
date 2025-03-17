package com.example.myweb.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.myweb.dto.request.UserCreatRequest;
import com.example.myweb.dto.request.UserRequest;
import com.example.myweb.dto.request.UserUpdateRequest;
import com.example.myweb.dto.respone.AuthenticationRespone;
import com.example.myweb.dto.respone.PageResponse;
import com.example.myweb.dto.respone.UserRespone;
import com.example.myweb.entity.Image;
import com.example.myweb.entity.Role;
import com.example.myweb.entity.User;
import com.example.myweb.exception.AppException;
import com.example.myweb.exception.ErrorCode;
import com.example.myweb.mapper.UserMapper;
import com.example.myweb.repository.ImageRepository;
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
    AuthenticationService authenticationService;
    ImageRepository imageRepository;

    @PostAuthorize("returnObject.username == authentication.name")
    public UserRespone getMyInfo() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userMapper.toUserRespone(
                userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public AuthenticationRespone creatUser(UserCreatRequest request) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(request.getUsername())))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole(roleRepository.findByName("USER"));

        user = userRepository.save(user);
        return AuthenticationRespone.builder().user(userMapper.toUserRespone(user))
                .token(authenticationService.generateToken(user))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserRespone createAdminUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername()).booleanValue())
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toAdminUser(request);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        user.setRole(roleRepository.findByName(request.getRole()));
        user = userRepository.save(user);

        if (StringUtils.hasText(request.getAvatar())) {
            Image avatar = Image.builder()
                    .name(request.getAvatar())
                    .user(user)
                    .build();
            avatar = imageRepository.save(avatar);
            user.setAvatar(avatar);
        }

        return userMapper.toUserRespone(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserRespone updateAdminUser(String id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateAdminUser(user, request);
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        }
        if (StringUtils.hasText(request.getAvatar())) {
            user.setAvatar(Image.builder().name(request.getAvatar()).user(user).build());
        }
        Role role = roleRepository.findByName(request.getRole());
        user.setRole(role);
        return userMapper.toUserRespone(userRepository.save(user));
    }

    // @PreAuthorize("hasAuthority('33333')")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserRespone> getAllUser() {
        return userMapper.toUserResponeList(userRepository.findAll());
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        if (StringUtils.hasText(request.getAvatar())) {
            user.setAvatar(Image.builder().name(request.getAvatar()).user(user).build());
        }

        return userMapper.toUserRespone(userRepository.save(user));
    }

    public PageResponse<UserRespone> getUsers(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userMapper.toPageUserRespone(userRepository.findAll(pageable));
    }

    public PageResponse<UserRespone> searchUser(int page, int size, String sortBy, String direction,
            String keyword) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userMapper.toPageUserRespone(userRepository.searchByKeyword(keyword, pageable));
    }

}
