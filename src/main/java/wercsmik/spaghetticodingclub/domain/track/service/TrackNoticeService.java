package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeUpdateRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackNotice;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackNoticeRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackParticipantsRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackNoticeService {

    private final TrackNoticeRepository trackNoticeRepository;
    private final TrackParticipantsRepository trackParticipantRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;

    @Transactional
    public TrackNoticeResponseDTO createTrackNotice(Long trackId, String userEmailOrUsername, TrackNoticeCreationRequestDTO requestDTO) {

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        validateNoticeContent(requestDTO);

        if (!isAdmin()) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        User user = userRepository.findByEmail(userEmailOrUsername)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        TrackNotice notice = buildTrackNotice(requestDTO, track, user);
        TrackNotice savedNotice = trackNoticeRepository.save(notice);

        return new TrackNoticeResponseDTO(savedNotice);
    }

    @Transactional(readOnly = true)
    public List<TrackNoticeResponseDTO> getNoticesForTrack(Long trackId) {

        if (!isAdmin()) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        return trackNoticeRepository.findAllByTrack_TrackIdIn(Collections.singleton(trackId)).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrackNoticeResponseDTO> getNoticesForUserByTrack(Long userId, Long trackId) {

        if (!trackParticipantRepository.existsById_UserIdAndId_TrackId(userId, trackId)) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        return trackNoticeRepository.findAllByTrack_TrackIdIn(Collections.singleton(trackId)).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TrackNoticeResponseDTO updateTrackNotice(Long trackId, Long noticeId, TrackNoticeUpdateRequestDTO requestDTO) {

        if (!isAdmin()) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        TrackNotice notice = trackNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOTICE_NOT_FOUND));

        notice.setTrackNoticeTitle(requestDTO.getTrackNoticeTitle());
        notice.setTrackNoticeContent(requestDTO.getTrackNoticeContent());

        TrackNotice updatedNotice = trackNoticeRepository.save(notice);

        return new TrackNoticeResponseDTO(updatedNotice);
    }


    @Transactional
    public void deleteTrackNotice(Long noticeId, Long trackId) {

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        TrackNotice notice = trackNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOTICE_NOT_FOUND));

        if (!isAdmin()) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        trackNoticeRepository.delete(notice);
    }


    /*
        공통 로직을 메서드로 분리
     */

    private void validateNoticeContent(TrackNoticeCreationRequestDTO requestDTO) {

        if (requestDTO.getTrackNoticeTitle() == null || requestDTO.getTrackNoticeTitle().trim().isEmpty() ||
                requestDTO.getTrackNoticeContent() == null || requestDTO.getTrackNoticeContent().trim().isEmpty()) {

            throw new CustomException(ErrorCode.INVALID_NOTICE_CONTENT);
        }
    }

    private boolean isAdmin() {

        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private TrackNotice buildTrackNotice(TrackNoticeCreationRequestDTO requestDTO, Track track, User user) {

        return TrackNotice.builder()
                .trackNoticeTitle(requestDTO.getTrackNoticeTitle())
                .trackNoticeContent(requestDTO.getTrackNoticeContent())
                .track(track)
                .user(user)
                .build();
    }

    private TrackNoticeResponseDTO convertToDTO(TrackNotice trackNotice) {

        return new TrackNoticeResponseDTO(trackNotice);
    }
}

