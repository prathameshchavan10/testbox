package com.testbox.service;

import java.util.List;

import com.testbox.dto.CreateStudentAnswerRequestDTO;
import com.testbox.dto.StudentAnswerResponseDTO;
import com.testbox.dto.UpdateStudentAnswerRequestDTO;

public interface StudentAnswerService {

    // Submit an answer
    StudentAnswerResponseDTO createStudentAnswer(
            CreateStudentAnswerRequestDTO request);

    // Fetch all submitted answers
    List<StudentAnswerResponseDTO> getAllStudentAnswers();

    // Fetch answer by ID
    StudentAnswerResponseDTO getStudentAnswerById(Long id);

    // Fetch all answers for an exam attempt
    List<StudentAnswerResponseDTO> getStudentAnswersByExamAttemptId(
            Long examAttemptId);
    
    // Update a student's answer
    StudentAnswerResponseDTO updateStudentAnswer(
            Long id,
            UpdateStudentAnswerRequestDTO request);

    // Delete submitted answer
    void deleteStudentAnswer(Long id);
    

}