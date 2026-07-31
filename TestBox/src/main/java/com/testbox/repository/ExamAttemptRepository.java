package com.testbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.ExamAttempt;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long>{

    // Check if a student has already attempted a particular exam
    boolean existsByStudentIdAndExamId(Long studentId, Long examId);

}
