package com.testbox.dto;

import com.testbox.enums.AnswerOption;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuestionRequestDTO {
	
	@NotBlank(message="Question text is required")
	private String questionText;
	
	@NotBlank(message="Option A is required")
	private String optionA;
	
	@NotBlank(message="Option B is required")
	private String optionB;
	
	@NotBlank(message="Option C is required")
	private String optionC;
	
	@NotBlank(message="Option D is required")
	private String optionD;
	
	//correct answer
	@NotNull(message="Correct answer is reqquired")
	private AnswerOption correctAnswer;
	
	//Marks allocated to this question
	@NotNull(message="Marks are required")
	@Min(value=1, message="Marks must be greater than 0")
	private Integer marks;
	
	//Exam to which this question belongs
	@NotNull(message="Exam ID is required")
	private Long examId;
	
	

}
