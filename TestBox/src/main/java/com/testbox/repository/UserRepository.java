package com.testbox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.User;
import com.testbox.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);
}