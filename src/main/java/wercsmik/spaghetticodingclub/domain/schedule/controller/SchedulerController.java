package wercsmik.spaghetticodingclub.domain.schedule.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerResponseDTO;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerUpdateRequestDTO;
import wercsmik.spaghetticodingclub.domain.schedule.service.SchedulerService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@RestController
@AllArgsConstructor
@RequestMapping("/schedules")
public class SchedulerController {

    private final SchedulerService schedulerService;

    @PostMapping
    public ResponseEntity<CommonResponse<SchedulerCreationResponseDTO>> createSchedule(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody SchedulerCreationRequestDTO requestDTO) {

        SchedulerCreationResponseDTO schedulerResponseDTO = schedulerService.createSchedule(userDetails, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("일정 생성 성공", schedulerResponseDTO));
    }

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<CommonResponse<List<SchedulerResponseDTO>>> getTeamSchedules(
            @PathVariable Long teamId) {

        List<SchedulerResponseDTO> schedules = schedulerService.getTeamSchedules(teamId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of("팀내 모든 일정 조회 성공", schedules));
    }

    @GetMapping("/teams/{teamId}/date-range")
    public ResponseEntity<CommonResponse<List<SchedulerResponseDTO>>> getTeamSchedulesByDateRange(
            @PathVariable Long teamId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<SchedulerResponseDTO> schedules = schedulerService.getTeamSchedulesByDateRange(teamId,
                startDate, endDate);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of("특정 날짜 범위 내 팀내 일정 조회 성공", schedules));
    }

    @PutMapping("/{schedulerId}")
    public ResponseEntity<CommonResponse<SchedulerResponseDTO>> updateSchedule(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody SchedulerUpdateRequestDTO requestDTO) {

        SchedulerResponseDTO schedulerResponseDTO = schedulerService.updateSchedule(schedulerId, userDetails, requestDTO);

        return ResponseEntity.status(HttpStatus.OK).
                body(CommonResponse.of("일정 수정 성공", schedulerResponseDTO));
    }
}