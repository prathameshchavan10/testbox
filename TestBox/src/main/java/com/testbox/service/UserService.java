package com.testbox.service;

import java.util.List;

import com.testbox.dto.LoginRequestDTO;
import com.testbox.dto.LoginResponseDTO;
import com.testbox.dto.RegisterRequestDTO;
import com.testbox.dto.UserResponseDTO;

public interface UserService {

	UserResponseDTO registerUser(RegisterRequestDTO request);
	
	LoginResponseDTO  loginUser(LoginRequestDTO request);
	
	UserResponseDTO getUserById(Long id);
	
	List<UserResponseDTO> getAllUsers();
}
