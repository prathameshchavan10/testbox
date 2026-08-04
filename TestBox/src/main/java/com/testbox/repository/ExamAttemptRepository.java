package com.testbox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.ExamAttempt;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    // Check if a student has already attempted a particular exam
    boolean existsByStudentIdAndExamId(
            Long studentId,
            Long examId);

    // Fetch all exam attempts of a particular student
    List<ExamAttempt> findByStudentId(Long studentId);

    // Fetch a specific exam attempt belonging to a particular student
    Optional<ExamAttempt> findByIdAndStudentId(
            Long id,
            Long studentId);
}