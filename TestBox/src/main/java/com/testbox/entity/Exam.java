package com.testbox.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.testbox.enums.ExamStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	 @Column(nullable = false)
    private String title;

	 @Column(length = 500)
    private String description;

	 @Column(nullable = false)
    private Integer durationMinutes;

	 @Column(nullable = false)
    private Integer totalMarks;

	 @Column(nullable = false)
    private Integer passingMarks;

	 @Column(nullable = false)
    private LocalDateTime startTime;

	 @Column(nullable = false)
    private LocalDateTime endTime;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ExamStatus status = ExamStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @OneToMany(mappedBy = "exam",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "exam")
    @Builder.Default
    private List<ExamAttempt> attempts = new ArrayList<>();
    
    // Automatically sets the creation timestamp before saving
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
