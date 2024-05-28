package wercsmik.spaghetticodingclub.domain.assessment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.assessment.dto.AssessmentRequestDTO;
import wercsmik.spaghetticodingclub.domain.assessment.dto.AssessmentResponseDTO;
import wercsmik.spaghetticodingclub.domain.assessment.entity.Assessment;
import wercsmik.spaghetticodingclub.domain.assessment.entity.AssessmentType;
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

        AssessmentType assessmentType;
        try {
            assessmentType = AssessmentType.valueOf(assessmentRequestDTO.getType());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.ASSESSMENT_TYPE_NOT_FOUND);
        }

        // 평가받는 사용자를 조회하는 로직
        User user = userRepository.findById(assessmentRequestDTO.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Assessment assessment = Assessment.builder()
                .userId(user) // 평가받는 사용자 설정
                .adminId(admin)
                .type(assessmentType)
                .content(assessmentRequestDTO.getContent())
                .build();

        Assessment savedAssessment = assessmentRepository.save(assessment);

        return AssessmentResponseDTO.of(savedAssessment);
    }

    @Transactional
    public List<AssessmentResponseDTO> getAllAssessment() {

        List<Assessment> assessments = assessmentRepository.findAll();

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
}
