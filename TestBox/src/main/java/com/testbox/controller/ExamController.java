package com.testbox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	//create a new Exam
	@PostMapping
	public ResponseEntity<ExamResponseDTO> createExam(
			@Valid @RequestBody CreateExamRequestDTO request)
	{
		ExamResponseDTO response = examService.createExam(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<ExamResponseDTO>> getAllExams() {

	    // Call Service Layer to fetch all Exams
	    List<ExamResponseDTO> exams = examService.getAllExams();

	    // Return the list of Exams with HTTP Status 200 (OK)
	    return ResponseEntity.ok(exams);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ExamResponseDTO> getExamById(@PathVariable Long id) {

	    // Call Service Layer to fetch Exam by ID
	    ExamResponseDTO response = examService.getExamById(id);

	    // Return the Exam with HTTP Status 200 (OK)
	    return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ExamResponseDTO> updateExam(
	        @PathVariable Long id,
	        @Valid @RequestBody UpdateExamRequestDTO request) {

	    // Call Service Layer to update the Exam
	    ExamResponseDTO response = examService.updateExam(id, request);

	    // Return the updated Exam with HTTP Status 200 (OK)
	    return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteExam(@PathVariable Long id) {

	    // Call Service Layer to delete the Exam
	    examService.deleteExam(id);

	    // Return success message
	    return ResponseEntity.ok("Exam deleted successfully.");
	}

}
