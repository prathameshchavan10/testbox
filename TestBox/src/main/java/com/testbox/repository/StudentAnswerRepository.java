package com.testbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.StudentAnswer;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {

    // Check if a question is already answered in an exam attempt
    boolean existsByExamAttemptIdAndQuestionId(Long examAttemptId, Long questionId);

    // Fetch all answers of a particular exam attempt
    List<StudentAnswer> findByExamAttemptId(Long examAttemptId);

}