package com.testbox.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.testbox.entity.User;
import com.testbox.enums.Role;
import com.testbox.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner{
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		
        // Check whether an admin already exists
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (!adminExists) {

            User admin = User.builder()
                    .name("System Administrator")
                    .email("admin@testbox.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();

            userRepository.save(admin);

            System.out.println("=======================================");
            System.out.println("Default Admin Created");
            System.out.println("Email    : admin@testbox.com");
            System.out.println("Password : Admin@123");
            System.out.println("=======================================");
        }
		
	}

}
