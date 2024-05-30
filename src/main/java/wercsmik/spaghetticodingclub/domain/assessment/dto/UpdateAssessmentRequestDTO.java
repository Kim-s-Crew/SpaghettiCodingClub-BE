package wercsmik.spaghetticodingclub.domain.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateAssessmentRequestDTO {

    private String background;

    private String guidance;

    private String relationship;

}
