package com.testbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.User;

public interface UserRespository extends JpaRepository<User, Long>{

	
}
