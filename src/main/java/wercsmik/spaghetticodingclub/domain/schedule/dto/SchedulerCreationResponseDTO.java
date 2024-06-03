package wercsmik.spaghetticodingclub.domain.schedule.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import wercsmik.spaghetticodingclub.domain.schedule.entity.Scheduler;

@Getter
@Setter
@AllArgsConstructor
public class SchedulerCreationResponseDTO {

    private Long schedulerId;

    private Long userId;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

        public static SchedulerCreationResponseDTO of(Scheduler scheduler) {
        return new SchedulerCreationResponseDTO(
                scheduler.getSchedulerId(),
                scheduler.getUserId().getUserId(),
                scheduler.getContent(),
                scheduler.getStartTime(),
                scheduler.getEndTime()
        );
    }
}
