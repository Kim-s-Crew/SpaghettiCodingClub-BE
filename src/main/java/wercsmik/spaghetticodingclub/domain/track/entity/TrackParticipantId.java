package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
public class TrackParticipantId implements Serializable {

    private Long userId;
    private Long trackId;

    public TrackParticipantId(Long userId, Long trackId) {
        this.userId = userId;
        this.trackId = trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackParticipantId)) return false;
        TrackParticipantId that = (TrackParticipantId) o;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getTrackId(), that.getTrackId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getTrackId());
    }
}