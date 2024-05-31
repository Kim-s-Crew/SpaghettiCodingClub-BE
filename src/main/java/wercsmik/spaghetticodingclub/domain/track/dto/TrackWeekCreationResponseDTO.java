package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class TrackWeekCreationResponseDTO {

    private Long trackWeekId;

    private String weekName;

    private LocalDate startDate;

    private LocalDate endDate;

}
