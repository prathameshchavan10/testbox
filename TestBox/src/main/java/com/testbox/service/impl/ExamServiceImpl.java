package com.testbox.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testbox.dto.CreateExamRequestDTO;
import com.testbox.dto.ExamResponseDTO;
import com.testbox.dto.UpdateExamRequestDTO;
import com.testbox.entity.Exam;
import com.testbox.entity.User;
import com.testbox.enums.Role;
import com.testbox.exception.ExamNotFoundException;
import com.testbox.exception.UserNotFoundException;
import com.testbox.repository.ExamRepository;
import com.testbox.repository.UserRepository;
import com.testbox.service.ExamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    @Override
    public ExamResponseDTO createExam(CreateExamRequestDTO request) {

        // Fetch teacher using teacherId
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() ->
                        new UserNotFoundException("Teacher not found"));

        // Validate business rules
        validateExam(request, teacher);

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

        // Fetch all Exams and convert them into ResponseDTOs
        return examRepository.findAll()
                .stream()
                .map(this::mapToExamResponseDTO)
                .toList();
    }

    @Override
    public ExamResponseDTO getExamById(Long id) {

        // Fetch Exam using ID
        Exam exam = examRepository.findById(id)
                .orElseThrow(() ->
                        new ExamNotFoundException("Exam not found"));

        // Convert Entity into ResponseDTO
        return mapToExamResponseDTO(exam);
    }

    @Override
    public ExamResponseDTO updateExam(Long id, UpdateExamRequestDTO request) {

        // Fetch Exam using ID
        Exam exam = examRepository.findById(id)
                .orElseThrow(() ->
                        new ExamNotFoundException("Exam not found"));

        // Fetch Teacher using teacherId
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() ->
                        new UserNotFoundException("Teacher not found"));

        // Validate business rules
        validateExam(request, teacher);

        // Update Exam details
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setPassingMarks(request.getPassingMarks());
        exam.setStartTime(request.getStartTime());
        exam.setEndTime(request.getEndTime());
        exam.setTeacher(teacher);

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

        // Delete Exam
        examRepository.delete(exam);
    }

    // Validate business rules before creating an Exam
    private void validateExam(CreateExamRequestDTO request, User teacher) {

        // Debug (remove after testing)
        System.out.println("Teacher ID   : " + teacher.getId());
        System.out.println("Teacher Name : " + teacher.getName());
        System.out.println("Teacher Role : " + teacher.getRole());

        // Only teachers can create exams
        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("Selected user is not a teacher");
        }

        // Passing marks cannot exceed total marks
        if (request.getPassingMarks() > request.getTotalMarks()) {
            throw new IllegalArgumentException("Passing marks cannot be greater than total marks");
        }

        // End time must be after start time
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after the start time");
        }
    }

    // Validate business rules before updating an Exam
    private void validateExam(UpdateExamRequestDTO request, User teacher) {

        // Debug (remove after testing)
        System.out.println("Teacher ID   : " + teacher.getId());
        System.out.println("Teacher Name : " + teacher.getName());
        System.out.println("Teacher Role : " + teacher.getRole());

        // Only teachers can update exams
        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("Selected user is not a teacher");
        }

        // Passing marks cannot exceed total marks
        if (request.getPassingMarks() > request.getTotalMarks()) {
            throw new IllegalArgumentException("Passing marks cannot be greater than total marks");
        }

        // End time must be after start time
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after the start time");
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
