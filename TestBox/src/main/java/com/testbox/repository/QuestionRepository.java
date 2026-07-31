package com.testbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{

	boolean existsByQuestionTextAndExamId(String questionText, Long examId);
	
	List<Question> findByExamId(Long examId);
}
