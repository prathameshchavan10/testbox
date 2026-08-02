package com.testbox.dto;

import java.time.LocalDateTime;

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
public class ResultResponseDTO {

    private Long id;

    private Long examAttemptId;

    private Long studentId;

    private String studentName;

    private Long examId;

    private String examTitle;

    private Integer totalMarks;

    private Integer obtainedMarks;

    private Double percentage;

    private Boolean passed;

    private LocalDateTime generatedAt;
}