package com.testbox.dto;

import com.testbox.enums.AnswerOption;

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
public class CreateStudentAnswerRequestDTO {

    // Exam attempt ID
    @NotNull(message = "Exam Attempt ID is required")
    private Long examAttemptId;

    // Question ID
    @NotNull(message = "Question ID is required")
    private Long questionId;

    // Selected answer
    @NotNull(message = "Selected answer is required")
    private AnswerOption selectedAnswer;
}