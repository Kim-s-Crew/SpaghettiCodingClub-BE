package wercsmik.spaghetticodingclub.domain.schedule.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import wercsmik.spaghetticodingclub.domain.schedule.entity.Scheduler;

@Getter
@Setter
@AllArgsConstructor
public class SchedulerResponseDTO {

    private Long schedulerId;

    private Long userId;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public static SchedulerResponseDTO of(Scheduler scheduler) {
        return new SchedulerResponseDTO(
                scheduler.getSchedulerId(),
                scheduler.getUserId().getUserId(),
                scheduler.getTitle(),
                scheduler.getStartTime(),
                scheduler.getEndTime()
        );
    }
}
