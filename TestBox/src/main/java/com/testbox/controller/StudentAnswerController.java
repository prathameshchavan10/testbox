package com.testbox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.testbox.dto.CreateStudentAnswerRequestDTO;
import com.testbox.dto.StudentAnswerResponseDTO;
import com.testbox.dto.UpdateStudentAnswerRequestDTO;
import com.testbox.service.StudentAnswerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student-answers")
@RequiredArgsConstructor
public class StudentAnswerController {

    private final StudentAnswerService studentAnswerService;

    // Submit an answer
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentAnswerResponseDTO createStudentAnswer(
            @Valid @RequestBody CreateStudentAnswerRequestDTO request) {

        return studentAnswerService.createStudentAnswer(request);
    }

    // Fetch all submitted answers
    @GetMapping
    public List<StudentAnswerResponseDTO> getAllStudentAnswers() {

        return studentAnswerService.getAllStudentAnswers();
    }

    // Fetch answer by ID
    @GetMapping("/{id}")
    public StudentAnswerResponseDTO getStudentAnswerById(
            @PathVariable Long id) {

        return studentAnswerService.getStudentAnswerById(id);
    }

    // Fetch all answers for a particular exam attempt
    @GetMapping("/exam-attempt/{examAttemptId}")
    public List<StudentAnswerResponseDTO> getStudentAnswersByExamAttemptId(
            @PathVariable Long examAttemptId) {

        return studentAnswerService.getStudentAnswersByExamAttemptId(examAttemptId);
    }
    
 // Update student's answer
    @PutMapping("/{id}")
    public StudentAnswerResponseDTO updateStudentAnswer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentAnswerRequestDTO request) {

        return studentAnswerService.updateStudentAnswer(id, request);
    }

    // Delete answer by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentAnswer(@PathVariable Long id) {

        studentAnswerService.deleteStudentAnswer(id);
    }
}