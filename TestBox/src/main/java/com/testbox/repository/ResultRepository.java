package com.testbox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.Result;


public interface ResultRepository extends JpaRepository<Result, Long> {

    // Check if a result already exists for an exam attempt
    boolean existsByExamAttemptId(Long examAttemptId);

    // Fetch result by exam attempt
    Optional<Result> findByExamAttemptId(Long examAttemptId);

    // Fetch all results of a student
    List<Result> findByStudentId(Long studentId);

    // Fetch all results of an exam
    List<Result> findByExamId(Long examId);
}