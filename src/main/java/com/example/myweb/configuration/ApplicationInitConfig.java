package com.example.myweb.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.myweb.entity.Role;
import com.example.myweb.entity.User;
import com.example.myweb.repository.RoleRepository;
import com.example.myweb.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                if (roleRepository.findByName("ADMIN") == null) {
                    roleRepository.save(Role.builder().name("ADMIN").description("ADMIN").build());
                }
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123456"))
                        .role(Role.builder().name("ADMIN").description("all permission").build())
                        .fullName("admin")
                        .build();
                userRepository.save(user);
            }
            if (roleRepository.findByName("USER") == null) {
                roleRepository.save(Role.builder().name("USER").description("USER").build());
            }

        };
    }
}
