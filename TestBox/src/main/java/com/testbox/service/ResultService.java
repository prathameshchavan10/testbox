package com.testbox.service;

import java.util.List;

import com.testbox.dto.GenerateResultRequestDTO;
import com.testbox.dto.ResultResponseDTO;

public interface ResultService {

    // Generate result for an exam attempt
    ResultResponseDTO generateResult(GenerateResultRequestDTO request);

    // Fetch all results
    List<ResultResponseDTO> getAllResults();

    // Fetch result by ID
    ResultResponseDTO getResultById(Long id);

    // Fetch result by exam attempt ID
    ResultResponseDTO getResultByExamAttemptId(Long examAttemptId);

    // Fetch all results of a student
    List<ResultResponseDTO> getResultsByStudentId(Long studentId);

    // Fetch all results of an exam
    List<ResultResponseDTO> getResultsByExamId(Long examId);

    // Delete result by ID
    void deleteResult(Long id);
}