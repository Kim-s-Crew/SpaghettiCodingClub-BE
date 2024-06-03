package wercsmik.spaghetticodingclub.domain.schedule.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.schedule.dto.SchedulerCreationResponseDTO;
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
}