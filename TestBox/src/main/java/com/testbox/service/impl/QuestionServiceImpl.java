package com.testbox.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.testbox.dto.CreateQuestionRequestDTO;
import com.testbox.dto.QuestionResponseDTO;
import com.testbox.dto.UpdateQuestionRequestDTO;
import com.testbox.entity.Exam;
import com.testbox.entity.Question;
import com.testbox.entity.User;
import com.testbox.exception.ExamNotFoundException;
import com.testbox.exception.QuestionNotFoundException;
import com.testbox.repository.ExamRepository;
import com.testbox.repository.QuestionRepository;
import com.testbox.security.CustomUserDetails;
import com.testbox.service.QuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
	
	
    /**
     * Returns the currently logged-in teacher.
     */
    private User getLoggedInTeacher() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
    }
    
    /**
     * Checks whether the logged-in teacher owns the exam.
     */
    private Exam getTeacherExam(Long examId) throws AccessDeniedException {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() ->
                        new ExamNotFoundException("Exam not found."));

        User loggedInTeacher = getLoggedInTeacher();

        if (!exam.getTeacher().getId().equals(loggedInTeacher.getId())) {
            throw new AccessDeniedException(
                    "You can manage questions only for your own exams.");
        }

        return exam;
    }
    
    /**
     * Prevent duplicate questions in the same exam.
     */
    private void validateDuplicateQuestion(
            String questionText,
            Long examId) {

        if (questionRepository.existsByQuestionTextAndExamId(
                questionText,
                examId)) {

            throw new IllegalArgumentException(
                    "Question already exists in this exam.");
        }
    }
    
    
    // ==========================================================
    // CREATE QUESTION
    // ==========================================================

    @Override
    public QuestionResponseDTO createQuestion(
            CreateQuestionRequestDTO request) {

        // Fetch the exam and verify ownership
        Exam exam = getTeacherExam(request.getExamId());

        // Check duplicate question
        validateDuplicateQuestion(
                request.getQuestionText(),
                request.getExamId());

        // Create Question Entity
        Question question = Question.builder()
                .questionText(request.getQuestionText())
                .optionA(request.getOptionA())
                .optionB(request.getOptionB())
                .optionC(request.getOptionC())
                .optionD(request.getOptionD())
                .correctAnswer(request.getCorrectAnswer())
                .marks(request.getMarks())
                .exam(exam)
                .build();

        // Save Question
        Question savedQuestion =
                questionRepository.save(question);

        // Convert Entity -> DTO
        return mapToQuestionResponseDTO(savedQuestion);
    }
    // ==========================================================
    // GET ALL QUESTIONS
    // ==========================================================

    @Override
    public List<QuestionResponseDTO> getAllQuestions() {

        User loggedInTeacher = getLoggedInTeacher();

        List<Exam> exams =
                examRepository.findByTeacherId(loggedInTeacher.getId());

        List<QuestionResponseDTO> questions = new ArrayList<>();

        for (Exam exam : exams) {

            List<Question> examQuestions =
                    questionRepository.findByExamId(exam.getId());

            questions.addAll(
                    examQuestions.stream()
                            .map(this::mapToQuestionResponseDTO)
                            .toList()
            );
        }

        return questions;
    }

    // ==========================================================
    // GET QUESTION BY ID
    // ==========================================================

    @Override
    public QuestionResponseDTO getQuestionById(Long id) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new QuestionNotFoundException("Question not found."));

        //verify logged in teacher owns the exam
        getTeacherExam(question.getExam().getId());
        
        //returns dto
        return mapToQuestionResponseDTO(question);
    }

    // ==========================================================
    // GET QUESTIONS BY EXAM
    // ==========================================================

    @Override
    public List<QuestionResponseDTO> getQuestionsByExam(Long examId) {

        // Verify exam exists
        examRepository.findById(examId)
                .orElseThrow(() ->
                        new ExamNotFoundException("Exam not found."));

        return questionRepository.findByExamId(examId)
                .stream()
                .map(this::mapToQuestionResponseDTO)
                .toList();
    }
    // ==========================================================
    // UPDATE QUESTION
    // ==========================================================

    @Override
    public QuestionResponseDTO updateQuestion(
            Long id,
            UpdateQuestionRequestDTO request) {

        // Fetch Question
        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new QuestionNotFoundException("Question not found."));

        // Verify logged-in teacher owns the exam
        Exam exam = getTeacherExam(request.getExamId());

        // If moving the question to another exam is not allowed
        if (!question.getExam().getId().equals(request.getExamId())) {
            throw new IllegalArgumentException(
                    "Question cannot be moved to another exam.");
        }

        // Check duplicate question
        if (!question.getQuestionText().equals(request.getQuestionText())
                && questionRepository.existsByQuestionTextAndExamId(
                        request.getQuestionText(),
                        request.getExamId())) {

            throw new IllegalArgumentException(
                    "Question already exists in this exam.");
        }

        // Update fields
        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setMarks(request.getMarks());
        question.setExam(exam);

        // Save
        Question updatedQuestion =
                questionRepository.save(question);

        // Return DTO
        return mapToQuestionResponseDTO(updatedQuestion);
    }
    

    // ==========================================================
    // DELETE QUESTION
    // ==========================================================

    @Override
    public void deleteQuestion(Long id) {

        // Fetch Question
        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new QuestionNotFoundException("Question not found."));

        // Verify logged-in teacher owns the exam
        getTeacherExam(question.getExam().getId());

        // Delete Question
        questionRepository.delete(question);
    }

    // ==========================================================
    // ENTITY -> DTO MAPPER
    // ==========================================================

    private QuestionResponseDTO mapToQuestionResponseDTO(
            Question question) {

        return QuestionResponseDTO.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .optionA(question.getOptionA())
                .optionB(question.getOptionB())
                .optionC(question.getOptionC())
                .optionD(question.getOptionD())
                .correctAnswer(question.getCorrectAnswer())
                .marks(question.getMarks())
                .examId(question.getExam().getId())
                .examTitle(question.getExam().getTitle())
                .build();
    }

}