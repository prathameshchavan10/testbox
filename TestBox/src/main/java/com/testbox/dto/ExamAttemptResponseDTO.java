package com.testbox.dto;

import java.time.LocalDateTime;

import com.testbox.enums.AttemptStatus;

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
public class ExamAttemptResponseDTO {

    private Long id;

    private Long studentId;
    
    private String studentName;

    private Long examId;
    
    private String examTitle;

    private LocalDateTime startTime;
    
    private LocalDateTime endTime;

    private Integer score;

    private AttemptStatus status;
}
