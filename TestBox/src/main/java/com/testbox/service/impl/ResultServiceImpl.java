package com.testbox.service.impl;




import java.util.List;

import org.springframework.stereotype.Service;

import com.testbox.dto.GenerateResultRequestDTO;
import com.testbox.dto.ResultResponseDTO;
import com.testbox.entity.ExamAttempt;
import com.testbox.entity.Result;
import com.testbox.entity.StudentAnswer;
import com.testbox.enums.AttemptStatus;
import com.testbox.exception.ExamAttemptNotFoundException;
import com.testbox.exception.ResultNotFoundException;
import com.testbox.repository.ExamAttemptRepository;
import com.testbox.repository.ResultRepository;
import com.testbox.repository.StudentAnswerRepository;
import com.testbox.service.ResultService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    @Override
    public ResultResponseDTO generateResult(GenerateResultRequestDTO request) {

        // Fetch the ExamAttempt using the examAttemptId received in the RequestDTO
        ExamAttempt examAttempt = examAttemptRepository.findById(request.getExamAttemptId())
                .orElseThrow(() ->
                        new ExamAttemptNotFoundException("Exam attempt not found"));

        // Validate business rules
        validateResultGeneration(examAttempt);

        // Fetch all StudentAnswers for the ExamAttempt
        List<StudentAnswer> studentAnswers =
                studentAnswerRepository.findByExamAttemptId(examAttempt.getId());

        // Calculate obtained marks
        Integer obtainedMarks = studentAnswers.stream()
                .mapToInt(StudentAnswer::getMarksAwarded)
                .sum();

        // Get total marks
        Integer totalMarks = examAttempt.getExam().getTotalMarks();

        // Calculate percentage
        Double percentage =
                (obtainedMarks * 100.0) / totalMarks;

        // Check pass/fail
        Boolean passed =
                obtainedMarks >= examAttempt.getExam().getPassingMarks();

        // Convert into Result Entity
        Result result = Result.builder()
                .examAttempt(examAttempt)
                .student(examAttempt.getStudent())
                .exam(examAttempt.getExam())
                .totalMarks(totalMarks)
                .obtainedMarks(obtainedMarks)
                .percentage(percentage)
                .passed(passed)
                .build();

        // Save Result Entity
        Result savedResult = resultRepository.save(result);

        // Convert Result Entity into ResultResponseDTO
        return mapToResultResponseDTO(savedResult);
    }

    @Override
    public List<ResultResponseDTO> getAllResults() {

        // Fetch all Result entities from the database
        // Convert the list of Result entities into ResultResponseDTOs
        return resultRepository.findAll()
                .stream()
                .map(this::mapToResultResponseDTO)
                .toList();
    }

    @Override
    public ResultResponseDTO getResultById(Long id) {

        // Fetch the Result Entity using the Result ID
        Result result = resultRepository.findById(id)
                .orElseThrow(() ->
                        new ResultNotFoundException("Result not found"));

        // Convert Result Entity into ResultResponseDTO
        return mapToResultResponseDTO(result);
    }

    @Override
    public ResultResponseDTO getResultByExamAttemptId(Long examAttemptId) {

        // Fetch Result using ExamAttempt ID
        Result result = resultRepository.findByExamAttemptId(examAttemptId)
                .orElseThrow(() ->
                        new ResultNotFoundException("Result not found"));

        // Convert Result Entity into ResultResponseDTO
        return mapToResultResponseDTO(result);
    }

    @Override
    public List<ResultResponseDTO> getResultsByStudentId(Long studentId) {

        // Fetch all Results of the Student
        return resultRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToResultResponseDTO)
                .toList();
    }

    @Override
    public List<ResultResponseDTO> getResultsByExamId(Long examId) {

        // Fetch all Results of the Exam
        return resultRepository.findByExamId(examId)
                .stream()
                .map(this::mapToResultResponseDTO)
                .toList();
    }

    @Override
    public void deleteResult(Long id) {

        // Fetch the Result Entity using the Result ID
        Result result = resultRepository.findById(id)
                .orElseThrow(() ->
                        new ResultNotFoundException("Result not found"));

        // Delete the Result Entity
        resultRepository.delete(result);
    }

    // Convert Result Entity into ResultResponseDTO
    private ResultResponseDTO mapToResultResponseDTO(Result result) {

        return ResultResponseDTO.builder()
                .id(result.getId())
                .examAttemptId(result.getExamAttempt().getId())
                .studentId(result.getStudent().getId())
                .studentName(result.getStudent().getName())
                .examId(result.getExam().getId())
                .examTitle(result.getExam().getTitle())
                .totalMarks(result.getTotalMarks())
                .obtainedMarks(result.getObtainedMarks())
                .percentage(result.getPercentage())
                .passed(result.getPassed())
                .generatedAt(result.getGeneratedAt())
                .build();
    }

    // Validate business rules before generating result
    private void validateResultGeneration(ExamAttempt examAttempt) {

        // Prevent duplicate result generation
        if (resultRepository.existsByExamAttemptId(examAttempt.getId())) {

            throw new IllegalArgumentException(
                    "Result has already been generated");
        }

        // Result can be generated only after exam submission
        if (examAttempt.getStatus() != AttemptStatus.SUBMITTED
                && examAttempt.getStatus() != AttemptStatus.AUTO_SUBMITTED) {

            throw new IllegalArgumentException(
                    "Exam has not been submitted yet");
        }
    }
}