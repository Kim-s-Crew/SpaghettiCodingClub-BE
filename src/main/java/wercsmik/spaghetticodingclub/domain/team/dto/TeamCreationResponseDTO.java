package wercsmik.spaghetticodingclub.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class TeamCreationResponseDTO {

    private Long teamId;

    private String teamName;

    private List<Map<String, Object>> members;
}
