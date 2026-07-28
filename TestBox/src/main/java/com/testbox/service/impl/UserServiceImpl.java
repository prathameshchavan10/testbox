package com.testbox.service.impl;

import java.util.List;
import java.util.stream.Collectors;

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
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	

	@Override
	public UserResponseDTO registerUser(RegisterRequestDTO request) {
		// Convert DTO to Entity
		User user = User.builder()
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())) //Encrypt using BCrypt later
				.role(request.getRole())
				.build();
		
		// Check if email already exists
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
		    throw new EmailAlreadyExistsException("Email already exists");
		}
		//Save user to database
		User savedUser = userRepository.save(user);
		
		//Convert entity to ResponseDTO
		return mapToUserResponseDTO(savedUser);
		
	}

	@Override
	public LoginResponseDTO loginUser(LoginRequestDTO request) {
		
		//Find user by email
		User user = userRepository.findByEmail(request.getEmail())
		.orElseThrow(()->new InvalidCredentialsException("Invalid email or password"));
		
		//verify password
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
		    throw new InvalidCredentialsException("Invalid email or password");
		}
		
		 // Generate JWT token
	    String token = jwtService.generateToken(user.getEmail());
		
	 // Return token along with user details
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
	                .orElseThrow(() -> new UserNotFoundException("User not found"));

		return mapToUserResponseDTO(user);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
	
		 
		// Fetch all users from the database
		// Convert List<User> into a Stream<User>
		// Convert each User entity into UserResponseDTO
		
	    return userRepository.findAll()
	    		.stream()
	    		.map(user -> mapToUserResponseDTO(user))
	    		.collect(Collectors.toList());
	}

    // Convert User Entity to UserResponseDTO
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
