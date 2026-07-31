package com.testbox.service;

import java.util.List;

import com.testbox.dto.CreateQuestionRequestDTO;
import com.testbox.dto.QuestionResponseDTO;
import com.testbox.dto.UpdateQuestionRequestDTO;

public interface QuestionService {

    // Create a new Question
    QuestionResponseDTO createQuestion(CreateQuestionRequestDTO request);

    // Fetch all Questions
    List<QuestionResponseDTO> getAllQuestions();

    // Fetch Question by ID
    QuestionResponseDTO getQuestionById(Long id);

    // Update an existing Question
    QuestionResponseDTO updateQuestion(Long id, UpdateQuestionRequestDTO request);

    // Delete Question by ID
    void deleteQuestion(Long id);
    
    List<QuestionResponseDTO> getQuestionsByExamId(Long examId);
}
