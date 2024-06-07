package wercsmik.spaghetticodingclub.domain.schedule.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerResponseDTO;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerUpdateRequestDTO;
import wercsmik.spaghetticodingclub.domain.schedule.entity.Scheduler;
import wercsmik.spaghetticodingclub.domain.schedule.repository.SchedulerRepository;
import wercsmik.spaghetticodingclub.domain.team.entity.Team;
import wercsmik.spaghetticodingclub.domain.team.entity.TeamMember;
import wercsmik.spaghetticodingclub.domain.team.repository.TeamMemberRepository;
import wercsmik.spaghetticodingclub.domain.team.repository.TeamRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@Service
@AllArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public SchedulerCreationResponseDTO createSchedule(UserDetailsImpl userDetails,
            SchedulerCreationRequestDTO requestDTO) {

        if (userDetails == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userDetails.getUser();

        LocalDateTime startTime = requestDTO.getStartTime();
        LocalDateTime endTime = requestDTO.getEndTime();

        // 종료 시간이 시작 시간보다 이전이면 예외 발생
        if (endTime.isBefore(startTime)) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

        // 사용자가 이미 일정이 겹치는지 확인
        boolean hasOverlap = schedulerRepository.existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(
                user, endTime, startTime);
        if (hasOverlap) {
            throw new CustomException(ErrorCode.SCHEDULE_OVERLAP);
        }

        Scheduler scheduler = Scheduler.builder()
                .userId(user)
                .title(requestDTO.getTitle())
                .startTime(requestDTO.getStartTime())
                .endTime(requestDTO.getEndTime())
                .build();

        Scheduler savedSchedule = schedulerRepository.save(scheduler);

        return SchedulerCreationResponseDTO.of(savedSchedule);
    }

    @Transactional(readOnly = true)
    public List<SchedulerResponseDTO> getTeamSchedules(Long teamId) {

        Team team = teamRepository.findById(teamId) // 팀 조회
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        List<TeamMember> teamMembers = teamMemberRepository.findByTeam_TeamId(teamId); // 팀 멤버 조회

        if (teamMembers.isEmpty()) {
            throw new CustomException(ErrorCode.TEAM_MEMBERS_NOT_FOUND);
        }

        // 팀 멤버들의 유저 리스트를 생성
        List<User> users = teamMembers.stream()
                .map(TeamMember::getUser)
                .collect(Collectors.toList());

        List<Scheduler> schedules = schedulerRepository.findByUserIdIn(users);

        return schedules.stream()
                .map(SchedulerResponseDTO::of)
                .collect(Collectors.toList());
    }

    public List<SchedulerResponseDTO> getTeamSchedulesByDateRange(Long teamId,
            LocalDateTime startDate, LocalDateTime endDate) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        List<TeamMember> teamMembers = teamMemberRepository.findByTeam_TeamId(teamId);

        if (teamMembers.isEmpty()) {
            throw new CustomException(ErrorCode.TEAM_MEMBERS_NOT_FOUND);
        }

        List<User> users = teamMembers.stream()
                .map(TeamMember::getUser)
                .collect(Collectors.toList());

        List<Scheduler> schedules = schedulerRepository.findByUserIdInAndDateRange(users, startDate,
                endDate);

        return schedules.stream()
                .map(SchedulerResponseDTO::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public SchedulerResponseDTO updateSchedule(Long schedulerId, UserDetailsImpl userDetails,
            SchedulerUpdateRequestDTO requestDTO) {

        if (userDetails == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        User user = userDetails.getUser();

        // 사용자가 해당 일정을 소유하고 있는지 확인
        if (!scheduler.getUserId().getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        LocalDateTime newStartTime = requestDTO.getStartTime();
        LocalDateTime newEndTime = requestDTO.getEndTime();

        // StartTime 또는 EndTime이 업데이트되는지 여부 확인
        boolean isTimeBeingUpdated = (newStartTime != null || newEndTime != null);

        // 새로운 시간이 제공되지 않은 경우 기존 시간 사용
        LocalDateTime startTime = (newStartTime != null) ? newStartTime : scheduler.getStartTime();
        LocalDateTime endTime = (newEndTime != null) ? newEndTime : scheduler.getEndTime();

        // 종료 시간이 시작 시간보다 이전이면 예외 발생
        if (isTimeBeingUpdated && endTime.isBefore(startTime)) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

        // 시간 업데이트가 있을 경우에만 겹침 검사 수행
        if (isTimeBeingUpdated) {
            boolean hasOverlap = schedulerRepository.existsByUserIdAndSchedulerIdNotAndTimeRangeOverlap(
                    user, schedulerId, startTime, endTime);
            if (hasOverlap) {
                throw new CustomException(ErrorCode.SCHEDULE_OVERLAP);
            }
        }

        // 제목이 null이 아니면 업데이트
        if (requestDTO.getTitle() != null) {
            scheduler.setTitle(requestDTO.getTitle());
        }
        scheduler.setStartTime(startTime);
        scheduler.setEndTime(endTime);

        Scheduler updatedScheduler = schedulerRepository.save(scheduler);

        return SchedulerResponseDTO.of(updatedScheduler);
    }

    @Transactional
    public void deleteSchedule(Long schedulerId, UserDetailsImpl userDetails) {

        Scheduler schedule = schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        // 사용자가 해당 일정을 소유하고 있는지 확인
        if (!schedule.getUserId().getUserId().equals(userDetails.getUser().getUserId())) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        schedulerRepository.delete(schedule);
    }
}