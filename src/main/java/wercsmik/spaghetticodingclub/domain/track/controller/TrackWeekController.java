package wercsmik.spaghetticodingclub.domain.track.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackWeekCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackWeekCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.service.TrackWeekService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

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
}
