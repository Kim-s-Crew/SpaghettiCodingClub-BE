package wercsmik.spaghetticodingclub.domain.assessment.service;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.assessment.dto.AssessmentRequestDTO;
import wercsmik.spaghetticodingclub.domain.assessment.dto.AssessmentResponseDTO;
import wercsmik.spaghetticodingclub.domain.assessment.dto.UpdateAssessmentRequestDTO;
import wercsmik.spaghetticodingclub.domain.assessment.entity.Assessment;
import wercsmik.spaghetticodingclub.domain.assessment.repository.AssessmentRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public AssessmentResponseDTO createAssessment(User admin, AssessmentRequestDTO assessmentRequestDTO) {

        // 평가받는 사용자를 조회하는 로직
        User user = userRepository.findById(assessmentRequestDTO.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // background, guidance, relationship 필드의 값을 검사하는 로직
        if (isNullOrEmpty(assessmentRequestDTO.getBackground()) &&
                isNullOrEmpty(assessmentRequestDTO.getGuidance()) &&
                isNullOrEmpty(assessmentRequestDTO.getRelationship())) {
            throw new CustomException(ErrorCode.INVALID_ASSESSMENT_DATA);
        }

        Assessment assessment = Assessment.builder()
                .userId(user) // 평가받는 사용자 설정
                .adminId(admin)
                .background(assessmentRequestDTO.getBackground())
                .guidance(assessmentRequestDTO.getGuidance())
                .relationship(assessmentRequestDTO.getRelationship())
                .build();

        Assessment savedAssessment = assessmentRepository.save(assessment);

        return AssessmentResponseDTO.of(savedAssessment);
    }

    @Transactional
    public List<AssessmentResponseDTO> getAllAssessment() {

        List<Assessment> assessments = assessmentRepository.findAll();

        if (assessments.isEmpty()) {
            throw new CustomException(ErrorCode.ASSESSMENT_NOT_FOUND);
        }

        return assessments.stream()
                .map(AssessmentResponseDTO::of)
                .collect(Collectors.toList());
    }

    public List<AssessmentResponseDTO> getAssessmentsByUserId(Long userId) {

        List<Assessment> assessments = assessmentRepository.findAllByUserId_UserId(userId);

        if (assessments == null || assessments.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return assessments.stream()
                .map(AssessmentResponseDTO::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssessmentResponseDTO updateAssessment(Long assessmentId, UpdateAssessmentRequestDTO updateAssessmentRequestDTO) {

        // 평가를 조회하는 로직
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ASSESSMENT_NOT_FOUND));

        // background, guidance, relationship 필드의 값이 null, Empty 이외 일때만 수정
        if (!isNullOrEmpty(updateAssessmentRequestDTO.getBackground())) {
            assessment.setBackground(updateAssessmentRequestDTO.getBackground());
        }
        if (!isNullOrEmpty(updateAssessmentRequestDTO.getGuidance())) {
            assessment.setGuidance(updateAssessmentRequestDTO.getGuidance());
        }
        if (!isNullOrEmpty(updateAssessmentRequestDTO.getRelationship())) {
            assessment.setRelationship(updateAssessmentRequestDTO.getRelationship());
        }

        Assessment updatedAssessment = assessmentRepository.save(assessment);

        return AssessmentResponseDTO.of(updatedAssessment);
    }

    @Transactional
    public void deleteAssessment(Long assessmentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ASSESSMENT_NOT_FOUND));

        assessmentRepository.delete(assessment);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
