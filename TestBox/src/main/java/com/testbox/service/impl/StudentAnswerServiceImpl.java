package com.testbox.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testbox.dto.CreateStudentAnswerRequestDTO;
import com.testbox.dto.StudentAnswerResponseDTO;
import com.testbox.dto.UpdateStudentAnswerRequestDTO;
import com.testbox.entity.ExamAttempt;
import com.testbox.entity.Question;
import com.testbox.entity.StudentAnswer;
import com.testbox.enums.AttemptStatus;
import com.testbox.exception.ExamAttemptNotFoundException;
import com.testbox.exception.QuestionNotFoundException;
import com.testbox.exception.StudentAnswerNotFoundException;
import com.testbox.repository.ExamAttemptRepository;
import com.testbox.repository.QuestionRepository;
import com.testbox.repository.StudentAnswerRepository;
import com.testbox.service.StudentAnswerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentAnswerServiceImpl implements StudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final QuestionRepository questionRepository;

    @Override
    public StudentAnswerResponseDTO createStudentAnswer(CreateStudentAnswerRequestDTO request) {

        // Fetch the ExamAttempt using the examAttemptId received in the RequestDTO
        ExamAttempt examAttempt = examAttemptRepository.findById(request.getExamAttemptId())
                .orElseThrow(() ->
                        new ExamAttemptNotFoundException("Exam attempt not found"));

        // Fetch the Question using the questionId received in the RequestDTO
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() ->
                        new QuestionNotFoundException("Question not found"));

        // Validate business rules
        validateStudentAnswer(examAttempt, question);

        // Check whether the selected answer is correct
        boolean isCorrect =
                request.getSelectedAnswer() == question.getCorrectAnswer();

        // Award marks only for correct answers
        Integer marksAwarded =
                isCorrect ? question.getMarks() : 0;

        // Convert CreateStudentAnswerRequestDTO into StudentAnswer Entity
        StudentAnswer studentAnswer = StudentAnswer.builder()
                .examAttempt(examAttempt)
                .question(question)
                .selectedAnswer(request.getSelectedAnswer())
                .isCorrect(isCorrect)
                .marksAwarded(marksAwarded)
                .build();

        // Save the StudentAnswer Entity
        StudentAnswer savedStudentAnswer =
                studentAnswerRepository.save(studentAnswer);

        // Convert StudentAnswer Entity into StudentAnswerResponseDTO
        return mapToStudentAnswerResponseDTO(savedStudentAnswer);
    }

    @Override
    public List<StudentAnswerResponseDTO> getAllStudentAnswers() {

        // Fetch all StudentAnswer entities from the database
        // Convert the list of StudentAnswer entities into StudentAnswerResponseDTOs
        return studentAnswerRepository.findAll()
                .stream()
                .map(this::mapToStudentAnswerResponseDTO)
                .toList();
    }

    @Override
    public StudentAnswerResponseDTO getStudentAnswerById(Long id) {

        // Fetch the StudentAnswer Entity using the StudentAnswer ID
        StudentAnswer studentAnswer = studentAnswerRepository.findById(id)
                .orElseThrow(() ->
                        new StudentAnswerNotFoundException("Student answer not found"));

        // Convert StudentAnswer Entity into StudentAnswerResponseDTO
        return mapToStudentAnswerResponseDTO(studentAnswer);
    }

    @Override
    public List<StudentAnswerResponseDTO> getStudentAnswersByExamAttemptId(Long examAttemptId) {

        // Check if the ExamAttempt exists
        examAttemptRepository.findById(examAttemptId)
                .orElseThrow(() ->
                        new ExamAttemptNotFoundException("Exam attempt not found"));

        // Fetch all StudentAnswers for the given ExamAttempt
        return studentAnswerRepository.findByExamAttemptId(examAttemptId)
                .stream()
                .map(this::mapToStudentAnswerResponseDTO)
                .toList();
    }

    @Override
    public void deleteStudentAnswer(Long id) {

        // Fetch the StudentAnswer Entity using the StudentAnswer ID
        StudentAnswer studentAnswer = studentAnswerRepository.findById(id)
                .orElseThrow(() ->
                        new StudentAnswerNotFoundException("Student answer not found"));

        // Delete the StudentAnswer Entity
        studentAnswerRepository.delete(studentAnswer);
    }

    // Convert StudentAnswer Entity into StudentAnswerResponseDTO
    private StudentAnswerResponseDTO mapToStudentAnswerResponseDTO(
            StudentAnswer studentAnswer) {

        return StudentAnswerResponseDTO.builder()
                .id(studentAnswer.getId())
                .examAttemptId(studentAnswer.getExamAttempt().getId())
                .questionId(studentAnswer.getQuestion().getId())
                .questionText(studentAnswer.getQuestion().getQuestionText())
                .selectedAnswer(studentAnswer.getSelectedAnswer())
                .isCorrect(studentAnswer.getIsCorrect())
                .marksAwarded(studentAnswer.getMarksAwarded())
                .build();
    }

    // Validate business rules before submitting an answer
    private void validateStudentAnswer(
            ExamAttempt examAttempt,
            Question question) {

        // Prevent answering the same question twice
        if (studentAnswerRepository.existsByExamAttemptIdAndQuestionId(
                examAttempt.getId(),
                question.getId())) {

            throw new IllegalArgumentException(
                    "Question has already been answered");
        }

        // Prevent answering after exam submission
        if (examAttempt.getStatus() == AttemptStatus.SUBMITTED
                || examAttempt.getStatus() == AttemptStatus.AUTO_SUBMITTED) {

            throw new IllegalArgumentException(
                    "Cannot answer after exam submission");
        }

        // Ensure the question belongs to the same exam
        if (!question.getExam().getId()
                .equals(examAttempt.getExam().getId())) {

            throw new IllegalArgumentException(
                    "Question does not belong to this exam");
        }
    }

	@Override
	public StudentAnswerResponseDTO updateStudentAnswer(Long id, UpdateStudentAnswerRequestDTO request) {
	    // Fetch the StudentAnswer using the StudentAnswer ID
	    StudentAnswer studentAnswer = studentAnswerRepository.findById(id)
	            .orElseThrow(() ->
	                    new StudentAnswerNotFoundException("Student answer not found"));

	    // Get the related ExamAttempt
	    ExamAttempt examAttempt = studentAnswer.getExamAttempt();

	    // Prevent updating answers after the exam has been submitted
	    if (examAttempt.getStatus() == AttemptStatus.SUBMITTED
	            || examAttempt.getStatus() == AttemptStatus.AUTO_SUBMITTED) {
	        throw new IllegalArgumentException(
	                "Cannot update answer after the exam has been submitted");
	    }

	    // Check whether the updated answer is correct
	    boolean isCorrect = request.getSelectedAnswer()
	            == studentAnswer.getQuestion().getCorrectAnswer();

	    // Update the selected answer
	    studentAnswer.setSelectedAnswer(request.getSelectedAnswer());

	    // Update correctness
	    studentAnswer.setIsCorrect(isCorrect);

	    // Update marks
	    studentAnswer.setMarksAwarded(
	            isCorrect ? studentAnswer.getQuestion().getMarks() : 0);

	    // Save the updated answer
	    StudentAnswer updatedStudentAnswer =
	            studentAnswerRepository.save(studentAnswer);

	    // Convert Entity into ResponseDTO
	    return mapToStudentAnswerResponseDTO(updatedStudentAnswer);
	}
}