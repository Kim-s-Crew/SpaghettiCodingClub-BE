package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TrackWeekCreationRequestDTO {

    private String weekName;

    private LocalDate startDate;

    private LocalDate endDate;
}
