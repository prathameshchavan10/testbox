package com.testbox.dto;

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
public class LoginResponseDTO {

    // JWT authentication token
    private String token;

    // Token type
    private String type;

    // User ID
    private Long id;

    // User's full name
    private String name;

    // User's registered email
    private String email;

    // User role
    private Role role;
}
