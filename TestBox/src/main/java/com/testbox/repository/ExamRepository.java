package com.testbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testbox.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long>{

}
