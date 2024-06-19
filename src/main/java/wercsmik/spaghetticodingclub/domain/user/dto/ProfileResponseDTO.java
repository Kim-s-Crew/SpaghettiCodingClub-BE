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

    private Long trackId;

    private String trackName;

    private Long currentTrackWeekId;

    private List<TrackWeekCreationResponseDTO> trackWeeks;

    private AssessmentResponseDTO assessment;


    public ProfileResponseDTO(User user, Long trackId, String trackName, Long currentTrackWeekId, List<TrackWeekCreationResponseDTO> trackWeeks, AssessmentResponseDTO assessment) {

        this.userId = user.getUserId();

        this.username = user.getUsername();

        this.email = user.getEmail();

        this.recommendEmail = user.getRecommendEmail();

        this.role = user.getRole();

        this.trackId = trackId;

        this.trackName = trackName;

        this.currentTrackWeekId = currentTrackWeekId;

        this.trackWeeks = trackWeeks;

        this.assessment = assessment;
    }
}
