package com.example.myweb.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 8, message = "USERNAME_INVALID")

    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    String image;
    List<String> roles;
}
