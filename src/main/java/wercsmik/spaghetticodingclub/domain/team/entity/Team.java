package wercsmik.spaghetticodingclub.domain.team.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackWeek;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trackWeekId", nullable = false)
    private TrackWeek trackWeek;

    @Column(length = 50, nullable = false)
    private String teamName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> members = new ArrayList<>();

    //TODO(은채): 팀리더 설정

    public static class TeamBuilder {
        public TeamBuilder members(List<User> members) {
            if (this.members == null) {
                this.members = new ArrayList<>();
            }
            this.members.addAll(members);
            return this;
        }
    }
}
