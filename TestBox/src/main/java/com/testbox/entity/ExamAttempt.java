package com.testbox.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.testbox.enums.AttemptStatus;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Ensures a student can attempt a particular exam only once.
@Table(
    name = "exam_attempts",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "exam_id"})
    }
)
public class ExamAttempt {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // Student who is attempting the exam
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "student_id", nullable = false)
	    private User student;

	    // Exam being attempted
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "exam_id", nullable = false)
	    private Exam exam;

	    // When the student started the exam
	    @Column(nullable = false, updatable = false)
	    private LocalDateTime startTime;

	    // When the student submitted the exam
	    private LocalDateTime endTime;

	    // Marks obtained by the student
	    @Builder.Default
	    @Column(nullable = false)
	    private Integer score = 0;

	    // Status of the attempt
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    @Builder.Default
	    private AttemptStatus status = AttemptStatus.STARTED;

	    // Answers submitted in this attempt
	    @OneToMany(
	            mappedBy = "examAttempt",
	            cascade = CascadeType.ALL,
	            orphanRemoval = true
	    )
	    @Builder.Default
	    private List<StudentAnswer> answers = new ArrayList<>();

	    @PrePersist
	    public void prePersist() {
	        startTime = LocalDateTime.now();
	    }
}