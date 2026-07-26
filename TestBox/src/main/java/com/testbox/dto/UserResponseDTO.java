package com.testbox.dto;

import java.time.LocalDateTime;

import com.testbox.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    // Unique user ID
    private Long id;

    // User's full name
    private String name;

    // User's registered email
    private String email;

    // User role (ADMIN, TEACHER, STUDENT)
    private Role role;

    // Account status
    private Boolean enabled;

    // Account creation timestamp
    private LocalDateTime createdAt;
}
