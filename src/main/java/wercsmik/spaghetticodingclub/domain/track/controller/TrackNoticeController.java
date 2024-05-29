package wercsmik.spaghetticodingclub.domain.track.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeUpdateRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.service.TrackNoticeService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks/{trackId}/notices")
public class TrackNoticeController {

    private final TrackNoticeService trackNoticeService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<TrackNoticeResponseDTO>> createTrackNotice(
            @PathVariable Long trackId,
            @RequestBody TrackNoticeCreationRequestDTO requestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmailOrUsername = authentication.getName();

        TrackNoticeResponseDTO createdNotice = trackNoticeService.createTrackNotice(trackId, userEmailOrUsername, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("트랙 공지 생성 성공", createdNotice));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TrackNoticeResponseDTO>>> getNotices(
            @PathVariable Long trackId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Long userId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getUserId();

        List<TrackNoticeResponseDTO> notices;
        if (isAdmin) {
            notices = trackNoticeService.getNoticesForTrack(trackId);  // 관리자는 모든 공지 조회
        } else {
            notices = trackNoticeService.getNoticesForUserByTrack(userId, trackId);  // 사용자는 해당 트랙의 공지만 조회
        }

        return ResponseEntity.ok().
                body(CommonResponse.of("트랙 공지 전체 조회 성공", notices));
    }

    @PutMapping("/{noticeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<TrackNoticeResponseDTO>> updateTrackNotice(
            @PathVariable Long trackId,
            @PathVariable Long noticeId,
            @RequestBody TrackNoticeUpdateRequestDTO requestDTO) {

        TrackNoticeResponseDTO updatedNotice = trackNoticeService.updateTrackNotice(trackId, noticeId, requestDTO);

        return ResponseEntity.ok().body(CommonResponse.of("트랙 공지 수정 성공", updatedNotice));
    }


    @DeleteMapping("/{noticeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<Void>> deleteTrackNotice(
            @PathVariable Long trackId,
            @PathVariable Long noticeId) {

        trackNoticeService.deleteTrackNotice(noticeId, trackId);

        return ResponseEntity.ok().body(CommonResponse.of("트랙 공지 삭제 성공", null));
    }
}
