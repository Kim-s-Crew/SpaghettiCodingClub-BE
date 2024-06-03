package wercsmik.spaghetticodingclub.domain.schedule.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.schedule.entity.Scheduler;
import wercsmik.spaghetticodingclub.domain.schedule.repository.SchedulerRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

@Service
@AllArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final UserRepository userRepository;

    @Transactional
    public SchedulerCreationResponseDTO createSchedule(User user, SchedulerCreationRequestDTO requestDTO) {

        LocalDateTime startTime = requestDTO.getStartTime();
        LocalDateTime endTime = requestDTO.getEndTime();

        // 종료 시간이 시작 시간보다 이전이면 예외 발생
        if (endTime.isBefore(startTime)) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

        // 사용자가 이미 일정이 겹치는지 확인
        boolean hasOverlap = schedulerRepository.existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(user, endTime, startTime);
        if (hasOverlap) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

        Scheduler scheduler = Scheduler.builder()
                .userId(user)
                .content(requestDTO.getContent())
                .startTime(requestDTO.getStartTime())
                .endTime(requestDTO.getEndTime())
                .build();

        Scheduler savedSchedule = schedulerRepository.save(scheduler);

        return SchedulerCreationResponseDTO.of(savedSchedule);
    }
}
