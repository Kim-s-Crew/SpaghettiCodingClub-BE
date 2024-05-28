package wercsmik.spaghetticodingclub.domain.assessment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.assessment.dto.AssessmentRequestDTO;
import wercsmik.spaghetticodingclub.domain.assessment.dto.AssessmentResponseDTO;
import wercsmik.spaghetticodingclub.domain.assessment.service.AssessmentService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assessmentItems")
public class AssessmentController{

    private final AssessmentService assessmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<AssessmentResponseDTO>> createAssessment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AssessmentRequestDTO assessmentRequestDTO) {

        AssessmentResponseDTO createdAssessment = assessmentService.createAssessment(userDetails.getUser(),
                assessmentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("평가 생성 성공", createdAssessment));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<List<AssessmentResponseDTO>>> getAllAssessment() {

        List<AssessmentResponseDTO> assessments = assessmentService.getAllAssessment();

        return ResponseEntity.ok().body(CommonResponse.of("평가 전체 조회 성공", assessments));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<List<AssessmentResponseDTO>>> getUserAssessment(
            @PathVariable Long userId) {

        List<AssessmentResponseDTO> assessments = assessmentService.getAssessmentsByUserId(userId);

        return ResponseEntity.ok().body(CommonResponse.of("특정 사용자 평가 조회 성공", assessments));
    }

}
