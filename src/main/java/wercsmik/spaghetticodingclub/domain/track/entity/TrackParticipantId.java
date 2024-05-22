package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
@Getter
public class TrackParticipantId implements Serializable {

    private Long userId;

    private Long trackId;
}
