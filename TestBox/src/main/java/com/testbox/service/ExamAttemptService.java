package com.testbox.service;

import java.util.List;

import com.testbox.dto.CreateExamAttemptRequestDTO;
import com.testbox.dto.ExamAttemptResponseDTO;

public interface ExamAttemptService {

    // Start an exam
    ExamAttemptResponseDTO createExamAttempt(CreateExamAttemptRequestDTO request);

    // Fetch all exam attempts
    List<ExamAttemptResponseDTO> getAllExamAttempts();

    // Fetch exam attempt by ID
    ExamAttemptResponseDTO getExamAttemptById(Long id);

    // Delete exam attempt
    void deleteExamAttempt(Long id);
}
