package com.testbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.ExamAttempt;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long>{

}
