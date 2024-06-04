package wercsmik.spaghetticodingclub.domain.track.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wercsmik.spaghetticodingclub.domain.track.dto.*;
import wercsmik.spaghetticodingclub.domain.track.service.TrackWeekService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tracks/{trackId}/weeks")
public class TrackWeekController {

    private final TrackWeekService trackWeekService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<TrackWeekCreationResponseDTO>> createTrackWeek(
            @PathVariable Long trackId,
            @RequestBody TrackWeekCreationRequestDTO requestDTO) {

        TrackWeekCreationResponseDTO trackWeek = trackWeekService.createTrackWeek(trackId, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("트랙 주차 생성 성공", trackWeek));
    }

    @GetMapping("/{weekId}")
    public ResponseEntity<CommonResponse<TrackWeekDetailResponseDTO>> getTrackWeekDetail(
            @PathVariable Long trackId,
            @PathVariable Long weekId) {

        TrackWeekDetailResponseDTO trackWeekDetail = trackWeekService.getTrackWeekDetail(trackId, weekId);

        return ResponseEntity.ok(CommonResponse.of("트랙 주차 상세 조회 성공", trackWeekDetail));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TrackWeekListResponseDTO>>> getAllTrackWeeks(
            @PathVariable Long trackId) {

        return ResponseEntity.ok()
                .body(CommonResponse.of("트랙 주차 목록 조회 성공", trackWeekService.findAllTrackWeeksByTrackId(trackId)));
    }


    @PutMapping("/{weekId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<TrackWeekUpdateResponseDTO>> updateTrackWeek(
            @PathVariable Long trackId,
            @PathVariable Long weekId,
            @RequestBody TrackWeekUpdateRequestDTO requestDTO) {

        TrackWeekUpdateResponseDTO trackWeek = trackWeekService.updateTrackWeek(trackId, weekId, requestDTO);

        return ResponseEntity.ok(CommonResponse.of("트랙 주차 수정 성공", trackWeek));
    }
}
