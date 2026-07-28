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
public class QuestionResponseDTO {

    // Question ID
    private Long id;

    // Question statement
    private String questionText;

    // Option A
    private String optionA;

    // Option B
    private String optionB;

    // Option C
    private String optionC;

    // Option D
    private String optionD;

    // Correct answer
    private AnswerOption correctAnswer;

    // Marks allotted to this question
    private Integer marks;

    // Exam details
    private Long examId;
    private String examTitle;
}
