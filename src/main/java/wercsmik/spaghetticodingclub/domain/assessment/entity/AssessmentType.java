package wercsmik.spaghetticodingclub.domain.assessment.entity;

import lombok.Getter;

@Getter
public enum AssessmentType {

    BACKGROUND("배경"),
    GUIDANCE("진도"),
    RELATIONSHIP("관계");

    private final String type;

    AssessmentType(String type) {
        this.type = type;
    }
}
