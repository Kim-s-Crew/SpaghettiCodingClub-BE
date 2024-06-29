package wercsmik.spaghetticodingclub.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnassignedUserResponseDTO {

    private Long userId;

    private String username;

    private String email;
}
