package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
public class TrackParticipantId implements Serializable {

    private Long userId;

    private Long trackId;

    public TrackParticipantId() {
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
