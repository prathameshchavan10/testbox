package com.testbox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.testbox.dto.CreateExamAttemptRequestDTO;
import com.testbox.dto.ExamAttemptResponseDTO;
import com.testbox.service.ExamAttemptService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exam-attempts")
@RequiredArgsConstructor
public class ExamAttemptController {
	private final ExamAttemptService examAttemptService;

    // Start a new exam attempt
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamAttemptResponseDTO createExamAttempt(
            @Valid @RequestBody CreateExamAttemptRequestDTO request) {

        return examAttemptService.createExamAttempt(request);
    }

    // Fetch all exam attempts
    @GetMapping
    public List<ExamAttemptResponseDTO> getAllExamAttempts() {

        return examAttemptService.getAllExamAttempts();
    }

    // Fetch exam attempt by ID
    @GetMapping("/{id}")
    public ExamAttemptResponseDTO getExamAttemptById(
            @PathVariable Long id) {

        return examAttemptService.getExamAttemptById(id);
    }

    // Delete exam attempt by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExamAttempt(
            @PathVariable Long id) {

        examAttemptService.deleteExamAttempt(id);
    }
}
