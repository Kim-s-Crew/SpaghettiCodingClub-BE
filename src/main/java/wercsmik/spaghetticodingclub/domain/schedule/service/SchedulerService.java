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
    public SchedulerCreationResponseDTO createSchedule(UserDetailsImpl userDetails, SchedulerCreationRequestDTO requestDTO) {

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
        boolean hasOverlap = schedulerRepository.existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(user, endTime, startTime);
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

    @Transactional(readOnly = true)
    public List<SchedulerResponseDTO> getTeamSchedulesByDateRange(Long teamId, LocalDateTime startDate, LocalDateTime endDate) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        List<TeamMember> teamMembers = teamMemberRepository.findByTeam_TeamId(teamId);

        if (teamMembers.isEmpty()) {
            throw new CustomException(ErrorCode.TEAM_MEMBERS_NOT_FOUND);
        }

        List<User> users = teamMembers.stream()
                .map(TeamMember::getUser)
                .collect(Collectors.toList());

        List<Scheduler> schedules = schedulerRepository.findByUserIdInAndDateRange(users, startDate, endDate);

        return schedules.stream()
                .map(SchedulerResponseDTO::of)
                .collect(Collectors.toList());
    }
}
