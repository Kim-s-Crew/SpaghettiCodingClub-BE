package wercsmik.spaghetticodingclub.domain.schedule.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerCreationRequestDTO {

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
