package com.testbox.service.impl;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.testbox.dto.CreateExamRequestDTO;
import com.testbox.dto.ExamResponseDTO;
import com.testbox.dto.UpdateExamRequestDTO;
import com.testbox.entity.Exam;
import com.testbox.entity.User;
import com.testbox.enums.Role;
import com.testbox.exception.ExamNotFoundException;
import com.testbox.repository.ExamRepository;
import com.testbox.repository.UserRepository;
import com.testbox.security.CustomUserDetails;
import com.testbox.service.ExamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    /**
     * Fetch the currently logged-in teacher.
     */
    private User getLoggedInTeacher() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User teacher = userDetails.getUser();

        if (teacher.getRole() != Role.TEACHER) {
            throw new AccessDeniedException(
                    "Only teachers can perform this operation.");
        }

        return teacher;
    }
    
    @Override
    public ExamResponseDTO createExam(CreateExamRequestDTO request) {

        // Fetch the logged-in teacher
        User teacher = getLoggedInTeacher();

        // Validate business rules
        validateExam(request);

        // Convert RequestDTO into Exam Entity
        Exam exam = Exam.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .totalMarks(request.getTotalMarks())
                .passingMarks(request.getPassingMarks())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .teacher(teacher)
                .build();

        // Save Exam into database
        Exam savedExam = examRepository.save(exam);

        // Convert Entity into ResponseDTO
        return mapToExamResponseDTO(savedExam);
    }

    @Override
    public List<ExamResponseDTO> getAllExams() {

        return examRepository.findAll()
                .stream()
                .map(this::mapToExamResponseDTO)
                .toList();
    }

    @Override
    public ExamResponseDTO getExamById(Long id) {

        Exam exam = examRepository.findById(id)
                .orElseThrow(() ->
                        new ExamNotFoundException("Exam not found"));

        return mapToExamResponseDTO(exam);
    }
    
    @Override
    public ExamResponseDTO updateExam(Long id, UpdateExamRequestDTO request) {

        // Fetch Exam using ID
        Exam exam = examRepository.findById(id)
                .orElseThrow(() ->
                        new ExamNotFoundException("Exam not found"));

        // Fetch logged-in teacher
        User teacher = getLoggedInTeacher();

        // Ensure the logged-in teacher owns this exam
        if (!exam.getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException(
                    "You can update only your own exams.");
        }

        // Validate business rules
        validateExam(request);

        // Update Exam details
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setPassingMarks(request.getPassingMarks());
        exam.setStartTime(request.getStartTime());
        exam.setEndTime(request.getEndTime());

        // Save updated Exam
        Exam updatedExam = examRepository.save(exam);

        // Convert Entity into ResponseDTO
        return mapToExamResponseDTO(updatedExam);
    }

    @Override
    public void deleteExam(Long id) {

        // Fetch Exam using ID
        Exam exam = examRepository.findById(id)
                .orElseThrow(() ->
                        new ExamNotFoundException("Exam not found"));

        // Fetch logged-in teacher
        User teacher = getLoggedInTeacher();

        // Ensure the logged-in teacher owns this exam
        if (!exam.getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException(
                    "You can delete only your own exams.");
        }

        // Delete Exam
        examRepository.delete(exam);
    }
    
    // Validate business rules before creating an Exam
    private void validateExam(CreateExamRequestDTO request) {

        // Passing marks cannot exceed total marks
        if (request.getPassingMarks() > request.getTotalMarks()) {
            throw new IllegalArgumentException(
                    "Passing marks cannot be greater than total marks");
        }

        // End time must be after start time
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException(
                    "End time must be after the start time");
        }
    }

    // Validate business rules before updating an Exam
    private void validateExam(UpdateExamRequestDTO request) {

        // Passing marks cannot exceed total marks
        if (request.getPassingMarks() > request.getTotalMarks()) {
            throw new IllegalArgumentException(
                    "Passing marks cannot be greater than total marks");
        }

        // End time must be after start time
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException(
                    "End time must be after the start time");
        }
    }

    // Convert Exam Entity into ResponseDTO
    private ExamResponseDTO mapToExamResponseDTO(Exam exam) {

        return ExamResponseDTO.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .durationMinutes(exam.getDurationMinutes())
                .totalMarks(exam.getTotalMarks())
                .passingMarks(exam.getPassingMarks())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .createdAt(exam.getCreatedAt())
                .status(exam.getStatus())
                .teacherId(exam.getTeacher().getId())
                .teacherName(exam.getTeacher().getName())
                .build();
    }
}