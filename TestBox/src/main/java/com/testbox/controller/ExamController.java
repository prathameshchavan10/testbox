package com.testbox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.testbox.dto.CreateExamRequestDTO;
import com.testbox.dto.ExamResponseDTO;
import com.testbox.dto.UpdateExamRequestDTO;
import com.testbox.service.ExamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@Validated
public class ExamController {

    private final ExamService examService;

    // ============================
    // CREATE EXAM
    // ============================

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponseDTO> createExam(
            @Valid @RequestBody CreateExamRequestDTO request) {

        ExamResponseDTO response = examService.createExam(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================
    // GET ALL EXAMS
    // ============================

    @GetMapping
    public ResponseEntity<List<ExamResponseDTO>> getAllExams() {

        return ResponseEntity.ok(examService.getAllExams());
    }

    // ============================
    // GET EXAM BY ID
    // ============================

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> getExamById(
            @PathVariable Long id) {

        return ResponseEntity.ok(examService.getExamById(id));
    }

    // ============================
    // UPDATE EXAM
    // ============================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponseDTO> updateExam(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExamRequestDTO request) {

        ExamResponseDTO response = examService.updateExam(id, request);

        return ResponseEntity.ok(response);
    }

    // ============================
    // DELETE EXAM
    // ============================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> deleteExam(
            @PathVariable Long id) {

        examService.deleteExam(id);

        return ResponseEntity.ok("Exam deleted successfully.");
    }
}