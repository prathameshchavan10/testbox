package com.testbox.service.impl;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import com.testbox.dto.CreateExamAttemptRequestDTO;
import com.testbox.dto.ExamAttemptResponseDTO;
import com.testbox.entity.Exam;
import com.testbox.entity.ExamAttempt;
import com.testbox.entity.User;
import com.testbox.enums.Role;
import com.testbox.exception.ExamAttemptAlreadyExistsException;
import com.testbox.exception.ExamAttemptNotFoundException;
import com.testbox.exception.ExamNotFoundException;
import com.testbox.repository.ExamAttemptRepository;
import com.testbox.repository.ExamRepository;
import com.testbox.security.CustomUserDetails;
import com.testbox.service.ExamAttemptService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamAttemptServiceImpl implements ExamAttemptService{
	
	private final ExamAttemptRepository examAttemptRepository;
	
	private final ExamRepository examRepository;
	
	/**
	 * Returns the currently logged-in student.
	 */
	private User getLoggedInStudent() {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    CustomUserDetails userDetails =
	            (CustomUserDetails) authentication.getPrincipal();

	    User student = userDetails.getUser();

	    if (student.getRole() != Role.STUDENT) {
	        throw new IllegalArgumentException(
	                "Only students can start exams.");
	    }

	    return student;
	}
	

	@Override
	public ExamAttemptResponseDTO createExamAttempt(CreateExamAttemptRequestDTO request) {
	    // Fetch the logged-in student
	    User student = getLoggedInStudent();

	    // Fetch the Exam
	    Exam exam = examRepository.findById(request.getExamId())
	            .orElseThrow(() ->
	                    new ExamNotFoundException("Exam not found"));

	    // Validate business rules
	    validateExamAttempt(student, exam);

	    // Create ExamAttempt Entity
	    ExamAttempt examAttempt = ExamAttempt.builder()
	            .student(student)
	            .exam(exam)
	            .build();

	    // Save
	    ExamAttempt savedExamAttempt =
	            examAttemptRepository.save(examAttempt);

	    // Return DTO
	    return mapToExamAttemptResponseDTO(savedExamAttempt);
	}
	
	
	@Override
	public List<ExamAttemptResponseDTO> getAllExamAttempts() {

	    // Fetch the logged-in student
	    User student = getLoggedInStudent();

	    // Fetch only this student's exam attempts
	    return examAttemptRepository.findByStudentId(student.getId())
	            .stream()
	            .map(this::mapToExamAttemptResponseDTO)
	            .toList();
	}
	
	
	@Override
	public ExamAttemptResponseDTO getExamAttemptById(Long id) {
        // Fetch the ExamAttempt Entity using the Attempt ID
        ExamAttempt examAttempt = examAttemptRepository.findById(id)
                .orElseThrow(() ->
                        new ExamAttemptNotFoundException("Exam attempt not found"));

        // Convert ExamAttempt Entity into ExamAttemptResponseDTO
        return mapToExamAttemptResponseDTO(examAttempt);
	}
	
	
	@Override
	public void deleteExamAttempt(Long id) {
        // Fetch the ExamAttempt Entity using the Attempt ID
        ExamAttempt examAttempt = examAttemptRepository.findById(id)
                .orElseThrow(() ->
                        new ExamAttemptNotFoundException("Exam attempt not found"));

        // Delete the ExamAttempt Entity
        examAttemptRepository.delete(examAttempt);
		
	}
	
	 // Convert ExamAttempt Entity into ExamAttemptResponseDTO
    private ExamAttemptResponseDTO mapToExamAttemptResponseDTO(ExamAttempt examAttempt) {

        return ExamAttemptResponseDTO.builder()
                .id(examAttempt.getId())
                .studentId(examAttempt.getStudent().getId())
                .studentName(examAttempt.getStudent().getName())
                .examId(examAttempt.getExam().getId())
                .examTitle(examAttempt.getExam().getTitle())
                .startTime(examAttempt.getStartTime())
                .endTime(examAttempt.getEndTime())
                .score(examAttempt.getScore())
                .status(examAttempt.getStatus())
                .build();
    }

    // Validate business rules before creating an exam attempt
    private void validateExamAttempt(User student, Exam exam) {

        // Only students can start an exam
        if (student.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("Selected user is not a student");
        }

        // Prevent multiple attempts for the same exam
        if (examAttemptRepository.existsByStudentIdAndExamId(
                student.getId(), exam.getId())) {

            throw new ExamAttemptAlreadyExistsException(
                    "You have already attempted this exam");
        }

        // Prevent starting an exam after it has ended
        System.out.println("Exam End Time : " + exam.getEndTime());
        System.out.println("Current Time  : " + LocalDateTime.now());
        if (exam.getEndTime().isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "The exam has already ended");
        }
    }

}
