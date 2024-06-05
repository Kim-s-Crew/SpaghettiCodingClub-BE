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
public class MultipleTeamCreationRequestDTO {

    private List<TeamCreationRequestDTO> teams;
}
