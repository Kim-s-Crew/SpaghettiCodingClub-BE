package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TrackParticipantResponseDTO {

    private Long userId;

    private String userName;

    private Long trackId;

    private String trackName;

    private LocalDateTime joinedAt;
}
