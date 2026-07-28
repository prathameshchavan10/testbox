package com.testbox.service;

import java.util.List;

import com.testbox.dto.CreateExamRequestDTO;
import com.testbox.dto.ExamResponseDTO;
import com.testbox.dto.UpdateExamRequestDTO;

public interface ExamService {
	
	 // Create a new Exam
	   ExamResponseDTO createExam(CreateExamRequestDTO request);

	// Fetch all Exams
	   List<ExamResponseDTO> getAllExams();

	// Fetch Exam by ID
	   ExamResponseDTO getExamById(Long id);

	// Update an existing Exam
	   ExamResponseDTO updateExam(Long id, UpdateExamRequestDTO request);

	// Delete Exam by ID
	   void deleteExam(Long id);
}
