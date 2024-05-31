package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackParticipantUpdateResponseDTO {

    private Long userId;

    private String updatedTrackName;
}
