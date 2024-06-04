package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.Builder;
import lombok.Getter;
import wercsmik.spaghetticodingclub.domain.team.dto.TeamDetailResponseDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class TrackWeekDetailResponseDTO {

    private Long trackWeekId;

    private String weekName;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<TeamDetailResponseDTO> teams;

    public TrackWeekDetailResponseDTO(Long trackWeekId, String weekName, LocalDate startDate, LocalDate endDate, List<TeamDetailResponseDTO> teams) {
        this.trackWeekId = trackWeekId;
        this.weekName = weekName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teams = teams;
    }

    public static class TrackWeekDetailResponseDTOBuilder {
    }
}
