package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrackRequestDTO {

    private final String trackName;
}
