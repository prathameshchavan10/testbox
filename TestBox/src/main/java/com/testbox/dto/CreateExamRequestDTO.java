package com.testbox.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateExamRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than zero")
    private Integer durationMinutes;

    @NotNull(message = "Total marks are required")
    @Min(value = 1, message = "Total marks must be greater than zero")
    private Integer totalMarks;

    @NotNull(message = "Passing marks are required")
    @Min(value = 0, message = "Passing marks cannot be negative")
    private Integer passingMarks;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;
}