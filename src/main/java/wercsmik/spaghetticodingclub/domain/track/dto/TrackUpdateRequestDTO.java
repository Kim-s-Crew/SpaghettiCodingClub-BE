package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TrackUpdateRequestDTO {

    private Long newTrackId;

    public TrackUpdateRequestDTO(Long newTrackId) {
        this.newTrackId = newTrackId;
    }
}
