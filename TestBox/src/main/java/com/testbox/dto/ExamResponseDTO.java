package com.testbox.dto;

import java.time.LocalDateTime;

import com.testbox.enums.ExamStatus;

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
public class ExamResponseDTO {

    private Long id;

    private String title;

    private String description;

    private Integer durationMinutes;

    private Integer totalMarks;

    private Integer passingMarks;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createdAt;

    private ExamStatus status;

    private Long teacherId;

    private String teacherName;
}
