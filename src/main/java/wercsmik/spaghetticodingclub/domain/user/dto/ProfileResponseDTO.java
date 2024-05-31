package wercsmik.spaghetticodingclub.domain.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import wercsmik.spaghetticodingclub.domain.assessment.dto.AssessmentResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackWeekCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;

@Getter
@AllArgsConstructor
public class ProfileResponseDTO {

    private Long userId;

    private String username;

    private String email;

    private String recommendEmail;

    private UserRoleEnum role;

    private String trackName;

    private List<TrackWeekCreationResponseDTO> trackWeeks;

    private List<AssessmentResponseDTO> assessments;


    public ProfileResponseDTO(User user, String trackName, List<TrackWeekCreationResponseDTO> trackWeeks, List<AssessmentResponseDTO> assessments) {

        this.userId = user.getUserId();

        this.username = user.getUsername();

        this.email = user.getEmail();

        this.recommendEmail = user.getRecommendEmail();

        this.role = user.getRole();

        this.trackName = trackName;

        this.trackWeeks = trackWeeks;

        this.assessments = assessments;
    }
}
