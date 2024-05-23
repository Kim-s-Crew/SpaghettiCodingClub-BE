package wercsmik.spaghetticodingclub.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;

@Getter
@AllArgsConstructor
public class ProfileResponseDTO {

    private Long userId;

    private String username;

    private String email;

    private String track;

    private UserRoleEnum role;

    private String recommendEmail;

    public ProfileResponseDTO(User user) {

        this.userId = user.getUserId();

        this.username = user.getUsername();

        this.email = user.getEmail();

        this.track = user.getTrack();

        this.role = user.getRole();

        this.recommendEmail = user.getRecommendEmail();
    }
}
