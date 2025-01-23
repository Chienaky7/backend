package com.example.myweb.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.myweb.entity.User;
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
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                // var roles = new HashSet<String>();
                // roles.add("ADMIN");
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123456"))
                        // .roles(roles)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
