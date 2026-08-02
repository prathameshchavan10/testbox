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
public class UpdateStudentAnswerRequestDTO {

    @NotNull(message = "Selected answer is required")
    private AnswerOption selectedAnswer;
}
