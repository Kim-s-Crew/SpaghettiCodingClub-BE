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

        if (userDetails == null) {
            System.out.println("UserDetails is null");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        System.out.println("SchedulerController: createSchedule called with user: " + userDetails.getUsername());
        SchedulerCreationResponseDTO schedulerResponseDTO = schedulerService.createSchedule(userDetails.getUser(), requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("일정 생성 성공", schedulerResponseDTO));
    }

//    @PutMapping("/{weekId}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<CommonResponse<TrackWeekUpdateResponseDTO>> updateTrackWeek(
//            @PathVariable Long trackId,
//            @PathVariable Long weekId,
//            @RequestBody TrackWeekUpdateRequestDTO requestDTO) {
//
//        TrackWeekUpdateResponseDTO trackWeek = trackWeekService.updateTrackWeek(trackId, weekId, requestDTO);
//
//        return ResponseEntity.ok(CommonResponse.of("트랙 주차 수정 성공", trackWeek));
//    }
}