package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wercsmik.spaghetticodingclub.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class TrackParticipants {

    @EmbeddedId
    private TrackParticipantId id;

    @MapsId("userId") // TrackParticipantId의 userId 필드를 매핑
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @MapsId("trackId") // TrackParticipantId의 trackId 필드를 매핑
    @ManyToOne
    @JoinColumn(name = "trackId", referencedColumnName = "id")
    private Track track;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    public static TrackParticipantsBuilder builder() {
        return new TrackParticipantsBuilder();
    }

    public static class TrackParticipantsBuilder {
        private User user;
        private Track track;
        private LocalDateTime joinedAt;

        public TrackParticipantsBuilder user(User user) {
            this.user = user;
            return this;
        }

        public TrackParticipantsBuilder track(Track track) {
            this.track = track;
            return this;
        }

        public TrackParticipantsBuilder joinedAt(LocalDateTime joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        public TrackParticipants build() {
            TrackParticipants trackParticipants = new TrackParticipants();
            trackParticipants.user = this.user;
            trackParticipants.track = this.track;
            trackParticipants.joinedAt = this.joinedAt;
            return trackParticipants;
        }
    }
}
