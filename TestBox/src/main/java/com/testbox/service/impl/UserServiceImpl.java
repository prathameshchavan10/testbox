package com.testbox.service.impl;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.testbox.dto.LoginRequestDTO;
import com.testbox.dto.LoginResponseDTO;
import com.testbox.dto.RegisterRequestDTO;
import com.testbox.dto.UserResponseDTO;
import com.testbox.entity.User;
import com.testbox.exception.EmailAlreadyExistsException;
import com.testbox.exception.InvalidCredentialsException;
import com.testbox.exception.UserNotFoundException;
import com.testbox.repository.UserRepository;
import com.testbox.security.JwtService;
import com.testbox.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO request) {

        // Check if email already exists
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException("Email already exists");
                });

        // Convert DTO to Entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();

        // Save User
        User savedUser = userRepository.save(user);

        // Convert Entity to DTO
        return mapToUserResponseDTO(savedUser);
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

        } catch (BadCredentialsException ex) {

            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Fetch user from database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // Generate JWT
        String token = jwtService.generateToken(user.getEmail());

        // Return complete login response
        return LoginResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        return mapToUserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponseDTO)
                .toList();
    }

    // Convert Entity to ResponseDTO
    private UserResponseDTO mapToUserResponseDTO(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}