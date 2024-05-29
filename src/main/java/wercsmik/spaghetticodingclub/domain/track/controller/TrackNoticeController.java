package wercsmik.spaghetticodingclub.domain.track.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.service.TrackNoticeService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks/{trackId}/notices")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class TrackNoticeController {

    private final TrackNoticeService trackNoticeService;

    @PostMapping
    public ResponseEntity<CommonResponse<TrackNoticeResponseDTO>> createTrackNotice(
            @PathVariable Long trackId,
            @RequestBody TrackNoticeCreationRequestDTO requestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmailOrUsername = authentication.getName();

        TrackNoticeResponseDTO createdNotice = trackNoticeService.createTrackNotice(trackId, userEmailOrUsername, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("트랙 공지 생성 성공", createdNotice));
    }
}
