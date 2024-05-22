package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class TrackParticipants extends BaseTimeEntity {

    @Id
    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name="trackId", nullable = false)
    private Track track;

    @Column(nullable = false)
    private LocalDateTime joinedAt;
}
