package wercsmik.spaghetticodingclub.domain.track.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackParticipantResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackParticipantUpdateResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackUpdateRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.service.TrackParticipantsService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trackParticipants")
public class TrackParticipantsController {

    private final TrackParticipantsService trackParticipantsService;

    @GetMapping("/{trackId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<List<TrackParticipantResponseDTO>>> getParticipantsByTrackId(
            @PathVariable Long trackId) {

        List<TrackParticipantResponseDTO> participants = trackParticipantsService.getParticipantsByTrack(trackId);

        return ResponseEntity.ok().body(CommonResponse.of("트랙 참여자 조회 성공", participants));
    }

    @PutMapping("/{userId}/{oldTrackId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<TrackParticipantUpdateResponseDTO>> updateParticipantTrack(
            @PathVariable Long userId,
            @PathVariable Long oldTrackId,
            @RequestBody TrackUpdateRequestDTO trackUpdateRequest) {

        TrackParticipantUpdateResponseDTO updatedInfo = trackParticipantsService.updateParticipantTrack(
                userId, oldTrackId, trackUpdateRequest.getNewTrackName());

        return ResponseEntity.ok().body(CommonResponse.of("트랙 참가자의 트랙 수정 성공", updatedInfo));
    }
}
