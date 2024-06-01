package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TrackWeekUpdateResponseDTO {

    private final Long trackWeekId;

    private final String weekName;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private TrackWeekUpdateResponseDTO(Builder builder) {
        this.trackWeekId = builder.trackWeekId;
        this.weekName = builder.weekName;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
    }

    public static class Builder {
        private Long trackWeekId;
        private String weekName;
        private LocalDate startDate;
        private LocalDate endDate;

        public Builder trackWeekId(Long trackWeekId) {
            this.trackWeekId = trackWeekId;
            return this;
        }

        public Builder weekName(String weekName) {
            this.weekName = weekName;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public TrackWeekUpdateResponseDTO build() {
            return new TrackWeekUpdateResponseDTO(this);
        }
    }
}
