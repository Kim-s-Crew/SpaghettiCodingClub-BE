package wercsmik.spaghetticodingclub.domain.track.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackUpdateResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.service.TrackService;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks")
public class TrackController extends BaseTimeEntity {

    private final TrackService trackService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<TrackResponseDTO>> createTrack(
            @RequestBody TrackRequestDTO trackRequestDTO) {

        TrackResponseDTO createdTrack = trackService.createTrack(trackRequestDTO);

        return ResponseEntity.ok().body(CommonResponse.of("트랙 생성 성공", createdTrack));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TrackResponseDTO>>> getAllTracks() {

        List<TrackResponseDTO> tracks = trackService.getAllTracks();

        return ResponseEntity.ok().body(CommonResponse.of("트랙 전체 조회 성공", tracks));
    }

    @PutMapping("/{trackId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<TrackUpdateResponseDTO>> updateTrack(
            @PathVariable Long trackId,
            @RequestBody TrackRequestDTO trackRequest) {

        TrackUpdateResponseDTO updatedTrack = trackService.updateTrackName(trackId, trackRequest.getTrackName());

        return ResponseEntity.ok().body(CommonResponse.of("트랙명 수정 성공", updatedTrack));
    }

}
