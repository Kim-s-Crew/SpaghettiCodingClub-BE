package wercsmik.spaghetticodingclub.domain.assessment.dto;

import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import wercsmik.spaghetticodingclub.domain.assessment.entity.Assessment;

@AllArgsConstructor
@Getter
public class AssessmentResponseDTO {

    private Long assessmentId;

    private Long userId; // 평가 받은 사용자 ID

    private Long adminId; // 평가를 한 관리자 ID

    @Column
    private String background;

    @Column
    private String guidance;

    @Column
    private String relationship;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public static AssessmentResponseDTO of (Assessment assessment) {
         return new AssessmentResponseDTO(
                assessment.getAssessmentId(),
                assessment.getUserId().getUserId(),
                assessment.getAdminId().getUserId(),
                assessment.getBackground(),
                assessment.getGuidance(),
                assessment.getRelationship(),
                assessment.getCreatedAt(),
                assessment.getModifiedAt()
         );
    }
}
