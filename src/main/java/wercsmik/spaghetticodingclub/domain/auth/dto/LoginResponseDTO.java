package wercsmik.spaghetticodingclub.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    private Long userId;

    private String username;

    private String email;

    private String track;

    private UserRoleEnum role;

    private String recommendEmail;

    public LoginResponseDTO(User user, String trackName) {

        this.userId = user.getUserId();

        this.username = user.getUsername();

        this.email = user.getEmail();

        this.track = trackName;

        this.role = user.getRole();

        this.recommendEmail = user.getRecommendEmail();
    }
}
