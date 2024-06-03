package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class TrackWeekListResponseDTO {

    private Long trackWeekId;

    private String weekName;

    private LocalDate startDate;

    private LocalDate endDate;
}
