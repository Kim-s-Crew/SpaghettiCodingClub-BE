package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackWeek;

@Getter
@Builder
public class TrackWeekCreationResponseDTO {

    private Long trackWeekId;

    private String weekName;

    private LocalDate startDate;

    private LocalDate endDate;

    public static TrackWeekCreationResponseDTO from(TrackWeek trackWeek) {
        return TrackWeekCreationResponseDTO.builder()
                .trackWeekId(trackWeek.getTrackWeekId())
                .weekName(trackWeek.getWeekName())
                .startDate(trackWeek.getStartDate())
                .endDate(trackWeek.getEndDate())
                .build();
    }
}
