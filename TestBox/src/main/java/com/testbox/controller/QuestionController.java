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

import com.testbox.dto.CreateQuestionRequestDTO;
import com.testbox.dto.QuestionResponseDTO;
import com.testbox.dto.UpdateQuestionRequestDTO;
import com.testbox.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

	public final QuestionService questionService;
	
	// Create a new Question
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponseDTO createQuestion(
            @Valid @RequestBody CreateQuestionRequestDTO request) {

        return questionService.createQuestion(request);
    }
    
    // Fetch all Questions
    @GetMapping
    public List<QuestionResponseDTO> getAllQuestions() {

        return questionService.getAllQuestions();
    }
    
    // Fetch Question by ID
    @GetMapping("/{id}")
    public QuestionResponseDTO getQuestionById(@PathVariable Long id) {

        return questionService.getQuestionById(id);
    }
    
    // Update an existing Question
    @PutMapping("/{id}")
    public QuestionResponseDTO updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuestionRequestDTO request) {

        return questionService.updateQuestion(id, request);
    }
    
    // Delete Question by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable Long id) {

        questionService.deleteQuestion(id);
    }
    
    @GetMapping("/exam/{examId}")
    public List<QuestionResponseDTO> getQuestionsByExamId(
            @PathVariable Long examId){
    	return null;
    }
}
