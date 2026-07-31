package com.testbox.entity;

import com.testbox.enums.AnswerOption;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswer {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // Attempt to which this answer belongs
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "exam_attempt_id", nullable = false)
	    private ExamAttempt examAttempt;

	    // Question being answered
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "question_id", nullable = false)
	    private Question question;

	    // Option selected by the student
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private AnswerOption selectedAnswer;

	    // Whether the selected answer is correct
	    @Builder.Default
	    @Column(nullable = false)
	    private Boolean isCorrect = false;

	    // Marks awarded for this question
	    @Builder.Default
	    @Column(nullable = false)
	    private Integer marksAwarded = 0;
}
