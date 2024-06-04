package wercsmik.spaghetticodingclub.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDetailResponseDTO {

    private Long teamId;

    private String teamName;

    private List<TeamMemberDetailResponseDTO> members;
}
