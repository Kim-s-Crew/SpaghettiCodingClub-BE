package wercsmik.spaghetticodingclub.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wercsmik.spaghetticodingclub.domain.assessment.entity.Assessment;
import wercsmik.spaghetticodingclub.domain.team.entity.Team;
import wercsmik.spaghetticodingclub.domain.team.entity.TeamMember;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Assessment> receivedAssessments = new ArrayList<>();

    @OneToMany(mappedBy = "adminId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Assessment> givenAssessments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TeamMember> teamMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

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

    public void setUsername(String username) {
        this.username = username;
    }
}
