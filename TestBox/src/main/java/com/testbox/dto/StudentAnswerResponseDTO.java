package com.testbox.dto;

import com.testbox.enums.AnswerOption;

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
public class StudentAnswerResponseDTO {

    private Long id;

    private Long examAttemptId;

    private Long questionId;

    private String questionText;

    private AnswerOption selectedAnswer;

    private Boolean isCorrect;

    private Integer marksAwarded;
}