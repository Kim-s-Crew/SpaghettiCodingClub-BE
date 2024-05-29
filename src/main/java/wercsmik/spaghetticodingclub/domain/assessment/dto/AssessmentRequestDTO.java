package wercsmik.spaghetticodingclub.domain.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRequestDTO {

    private Long userId;

    private String background;

    private String guidance;

    private String relationship;
}
