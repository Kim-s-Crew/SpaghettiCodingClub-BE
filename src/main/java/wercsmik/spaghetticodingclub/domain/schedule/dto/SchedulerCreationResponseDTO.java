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

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

        public static SchedulerCreationResponseDTO of(Scheduler scheduler) {
        return new SchedulerCreationResponseDTO(
                scheduler.getSchedulerId(),
                scheduler.getUserId().getUserId(),
                scheduler.getTitle(),
                scheduler.getStartTime(),
                scheduler.getEndTime(),
                scheduler.getCreatedAt(),
                scheduler.getModifiedAt()
        );
    }
}
