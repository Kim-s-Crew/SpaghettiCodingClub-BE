package wercsmik.spaghetticodingclub.domain.schedule.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerUpdateRequestDTO {

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}