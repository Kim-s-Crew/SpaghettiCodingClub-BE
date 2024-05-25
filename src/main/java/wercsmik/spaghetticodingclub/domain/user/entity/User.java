package wercsmik.spaghetticodingclub.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String recommendEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    private User(String username, String password, String email, String recommendEmail, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.recommendEmail = recommendEmail;
        this.role = role;
    }

    public void setPassword(String updatePassword) {
        this.password = updatePassword;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}
