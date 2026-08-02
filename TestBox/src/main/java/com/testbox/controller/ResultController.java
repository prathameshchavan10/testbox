package com.testbox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.testbox.dto.GenerateResultRequestDTO;
import com.testbox.dto.ResultResponseDTO;
import com.testbox.service.ResultService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    // Generate result
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultResponseDTO generateResult(
            @Valid @RequestBody GenerateResultRequestDTO request) {

        return resultService.generateResult(request);
    }

    // Fetch all results
    @GetMapping
    public List<ResultResponseDTO> getAllResults() {

        return resultService.getAllResults();
    }

    // Fetch result by ID
    @GetMapping("/{id}")
    public ResultResponseDTO getResultById(
            @PathVariable Long id) {

        return resultService.getResultById(id);
    }

    // Fetch result by exam attempt ID
    @GetMapping("/exam-attempt/{examAttemptId}")
    public ResultResponseDTO getResultByExamAttemptId(
            @PathVariable Long examAttemptId) {

        return resultService.getResultByExamAttemptId(examAttemptId);
    }

    // Fetch all results of a student
    @GetMapping("/student/{studentId}")
    public List<ResultResponseDTO> getResultsByStudentId(
            @PathVariable Long studentId) {

        return resultService.getResultsByStudentId(studentId);
    }

    // Fetch all results of an exam
    @GetMapping("/exam/{examId}")
    public List<ResultResponseDTO> getResultsByExamId(
            @PathVariable Long examId) {

        return resultService.getResultsByExamId(examId);
    }

    // Delete result
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResult(
            @PathVariable Long id) {

        resultService.deleteResult(id);
    }
}