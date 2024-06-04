package wercsmik.spaghetticodingclub.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDetailResponseDTO {

    private Long userId;

    private String userName;
}
