package com.testbox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.testbox.dto.CreateExamAttemptRequestDTO;
import com.testbox.dto.ExamAttemptResponseDTO;
import com.testbox.service.ExamAttemptService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exam-attempts")
@RequiredArgsConstructor
@Validated
public class ExamAttemptController {

    private final ExamAttemptService examAttemptService;

    // ==========================================================
    // START EXAM
    // ==========================================================

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ExamAttemptResponseDTO> createExamAttempt(
            @Valid @RequestBody CreateExamAttemptRequestDTO request) {

        ExamAttemptResponseDTO response =
                examAttemptService.createExamAttempt(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==========================================================
    // GET ALL MY EXAM ATTEMPTS
    // ==========================================================

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ExamAttemptResponseDTO>> getAllExamAttempts() {

        return ResponseEntity.ok(
                examAttemptService.getAllExamAttempts());
    }

    // ==========================================================
    // GET MY EXAM ATTEMPT BY ID
    // ==========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ExamAttemptResponseDTO> getExamAttemptById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examAttemptService.getExamAttemptById(id));
    }

    // ==========================================================
    // DELETE MY EXAM ATTEMPT
    // ==========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> deleteExamAttempt(
            @PathVariable Long id) {

        examAttemptService.deleteExamAttempt(id);

        return ResponseEntity.ok(
                "Exam attempt deleted successfully.");
    }
}