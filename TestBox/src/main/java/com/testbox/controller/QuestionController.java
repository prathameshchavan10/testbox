package com.testbox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.testbox.dto.CreateQuestionRequestDTO;
import com.testbox.dto.QuestionResponseDTO;
import com.testbox.dto.UpdateQuestionRequestDTO;
import com.testbox.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Validated
public class QuestionController {

    private final QuestionService questionService;

    // ==========================================================
    // CREATE QUESTION
    // ==========================================================

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QuestionResponseDTO> createQuestion(
            @Valid @RequestBody CreateQuestionRequestDTO request) {

        QuestionResponseDTO response =
                questionService.createQuestion(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==========================================================
    // GET ALL QUESTIONS
    // ==========================================================

    @GetMapping
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {

        return ResponseEntity.ok(
                questionService.getAllQuestions());
    }

    // ==========================================================
    // GET QUESTION BY ID
    // ==========================================================

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                questionService.getQuestionById(id));
    }

    // ==========================================================
    // GET QUESTIONS BY EXAM
    // ==========================================================

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<QuestionResponseDTO>>
            getQuestionsByExam(
                    @PathVariable Long examId) {

        return ResponseEntity.ok(
                questionService.getQuestionsByExam(examId));
    }

    // ==========================================================
    // UPDATE QUESTION
    // ==========================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuestionRequestDTO request) {

        QuestionResponseDTO response =
                questionService.updateQuestion(id, request);

        return ResponseEntity.ok(response);
    }

    // ==========================================================
    // DELETE QUESTION
    // ==========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> deleteQuestion(
            @PathVariable Long id) {

        questionService.deleteQuestion(id);

        return ResponseEntity.ok(
                "Question deleted successfully.");
    }
}