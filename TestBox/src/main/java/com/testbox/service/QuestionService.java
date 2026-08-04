package com.testbox.service;

import java.util.List;

import com.testbox.dto.CreateQuestionRequestDTO;
import com.testbox.dto.QuestionResponseDTO;
import com.testbox.dto.UpdateQuestionRequestDTO;

public interface QuestionService {

    // Create Question
    QuestionResponseDTO createQuestion(CreateQuestionRequestDTO request);

    // Get All Questions
    List<QuestionResponseDTO> getAllQuestions();

    // Get Question By Id
    QuestionResponseDTO getQuestionById(Long id);

    // Get Questions By Exam
    List<QuestionResponseDTO> getQuestionsByExam(Long examId);

    // Update Question
    QuestionResponseDTO updateQuestion(
            Long id,
            UpdateQuestionRequestDTO request);

    // Delete Question
    void deleteQuestion(Long id);
}