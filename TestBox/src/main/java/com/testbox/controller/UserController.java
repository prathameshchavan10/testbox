package com.testbox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testbox.dto.LoginRequestDTO;
import com.testbox.dto.LoginResponseDTO;
import com.testbox.dto.RegisterRequestDTO;
import com.testbox.dto.UserResponseDTO;
import com.testbox.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Register User (ADMIN only)
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody RegisterRequestDTO request) {

        UserResponseDTO response = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login (Public)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(
            @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response = userService.loginUser(request);

        return ResponseEntity.ok(response);
    }

    // Get User By Id (ADMIN only)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        UserResponseDTO response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }

    // Get All Users (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

}