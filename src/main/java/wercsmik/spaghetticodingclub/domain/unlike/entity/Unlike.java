package wercsmik.spaghetticodingclub.domain.unlike.entity;

import jakarta.persistence.*;
import lombok.*;
import wercsmik.spaghetticodingclub.domain.team.entity.Team;
import wercsmik.spaghetticodingclub.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "Unlikes")
public class Unlike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unlikeId;

    @ManyToOne
    @JoinColumn(name = "senderUserId", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiverUserId", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "teamId", nullable = false)
    private Team team;

    @Column(name = "cause", nullable = false, columnDefinition = "TEXT")
    private String cause;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
