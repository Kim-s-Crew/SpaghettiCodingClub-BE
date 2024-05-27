package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TrackUpdateRequestDTO {

    private String newTrackName;

    public TrackUpdateRequestDTO(String newTrackName) {
        this.newTrackName = newTrackName;
    }
}
