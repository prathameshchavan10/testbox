package com.testbox.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.testbox.dto.CreateQuestionRequestDTO;
import com.testbox.dto.QuestionResponseDTO;
import com.testbox.dto.UpdateQuestionRequestDTO;
import com.testbox.entity.Exam;
import com.testbox.entity.Question;
import com.testbox.exception.ExamNotFoundException;
import com.testbox.exception.QuestionNotFoundException;
import com.testbox.repository.ExamRepository;
import com.testbox.repository.QuestionRepository;
import com.testbox.service.QuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
	
	private final QuestionRepository questionRepository;
	private final ExamRepository examRepository;
	
	@Override
	public QuestionResponseDTO createQuestion(CreateQuestionRequestDTO request) {
		
		//Fetch the exam using the emaxId recieved in the the RequestDTO
		Exam exam = examRepository.findById(request.getExamId()).orElseThrow(()->new ExamNotFoundException("Exam not found"));
		
		// Validate business rules
		validateQuestion(request, exam);
		

		
		// Convert the CreateQuestionRequestDTO into a Question Entity
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

     // Save the Question Entity into the database
        Question savedQuestion = questionRepository.save(question);
        
     // Convert the saved Question Entity into QuestionResponseDTO
        return mapToQuestionResponseDTO(savedQuestion);
	}

	@Override
	public List<QuestionResponseDTO> getAllQuestions() {
		
        // Fetch all Question Entities from the database
        // Convert the list of Question Entities into QuestionResponseDTOs
        return questionRepository.findAll()
                .stream()
                .map(this::mapToQuestionResponseDTO)
                .toList();
	}

	@Override
	public QuestionResponseDTO getQuestionById(Long id) {
		
        // Fetch the Question Entity using the Question ID
        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new QuestionNotFoundException("Question not found"));

        // Convert Question Entity into QuestionResponseDTO
        return mapToQuestionResponseDTO(question);
	}

	@Override
	public QuestionResponseDTO updateQuestion(Long id, UpdateQuestionRequestDTO request) {
        // Fetch the Question Entity using the Question ID
        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new QuestionNotFoundException("Question not found"));

        // Fetch the Exam using the examId received in the RequestDTO
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() ->
                        new ExamNotFoundException("Exam not found"));
        
     // Validate business rules
        validateQuestion(request, exam);

        // Update the Question Entity with the new values
        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setMarks(request.getMarks());
        question.setExam(exam);

        // Save the updated Question Entity
        Question updatedQuestion = questionRepository.save(question);

        // Convert Question Entity into QuestionResponseDTO
        return mapToQuestionResponseDTO(updatedQuestion);
	
	}

	@Override
	public void deleteQuestion(Long id) {
        // Fetch the Question Entity using the Question ID
        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new QuestionNotFoundException("Question not found"));
        
     // Prevent deleting questions after the exam has started
        if (question.getExam().getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Cannot delete questions after the exam has started");
        }

        // Delete the Question Entity
        questionRepository.delete(question);
		
	}
	
	// Convert Question Entity into QuestionResponseDTO
    private QuestionResponseDTO mapToQuestionResponseDTO(Question question) {

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
    
    //validate business rules before creating a question
    private void validateQuestion(CreateQuestionRequestDTO request, Exam exam)
    {
//    	//Prevent duplicate questions in same exam
//    	if(questionRepository.existsByQuestionTextAndExamId(
//    			request.getQuestionText(), exam.getId())){
//    				
//    		throw new IllegalArgumentException(
//    				"Question already exists in this exam");
//    			}
    	
    	//prevent creating questions after the exam has started
        if(exam.getStartTime().isBefore(LocalDateTime.now()))
        {
        	  throw new IllegalArgumentException(
                      "Cannot add questions after the exam has started");
        }
    }
    
    // Validate business rules before updating a question
    private void validateQuestion(UpdateQuestionRequestDTO request, Exam exam) {

//        // Prevent duplicate questions in the same exam
//        if (questionRepository.existsByQuestionTextAndExamId(
//                request.getQuestionText(), exam.getId())) {
//
//            throw new IllegalArgumentException(
//                    "Question already exists in this exam");
//        }

        // Prevent updating questions after the exam has started
        if (exam.getStartTime().isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Cannot update questions after the exam has started");
        }
    }

	@Override
	public List<QuestionResponseDTO> getQuestionsByExamId(Long examId) {
		
	    // Check if the exam exists
	    examRepository.findById(examId)
	            .orElseThrow(() ->
	                    new ExamNotFoundException("Exam not found"));

	    // Fetch all questions for the given exam
	    return questionRepository.findByExamId(examId)
	            .stream()
	            .map(this::mapToQuestionResponseDTO)
	            .toList();
	}
    
    

}
