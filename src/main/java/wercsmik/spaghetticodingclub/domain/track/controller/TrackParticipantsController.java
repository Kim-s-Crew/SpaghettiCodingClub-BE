package wercsmik.spaghetticodingclub.domain.track.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackParticipantResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.service.TrackParticipantsService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/trackParticipants")
public class TrackParticipantsController {

    private final TrackParticipantsService trackParticipantsService;

    @GetMapping("/{trackId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<List<TrackParticipantResponseDTO>>> getParticipantsByTrackId(
            @PathVariable Long trackId) {

        List<TrackParticipantResponseDTO> participants = trackParticipantsService.getParticipantsByTrack(trackId);

        return ResponseEntity.ok().body(CommonResponse.of("트랙 참가자 조회 성공", participants));
    }
}
