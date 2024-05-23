package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TrackParticipantResponseDTO {

    private Long userId;

    private String userName;

    private Long trackId;

    private String trackName;

    private LocalDateTime joinedAt;
}
