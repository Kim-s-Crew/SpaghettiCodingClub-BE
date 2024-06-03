package wercsmik.spaghetticodingclub.domain.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TeamCreationRequestDTO {

    private String teamName;

    private List<Long> memberIds;
}
