package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackNoticeResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackNotice;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackNoticeRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class TrackNoticeService {

    private final TrackNoticeRepository trackNoticeRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;

    @Transactional
    public TrackNoticeResponseDTO createTrackNotice(Long trackId, String userEmailOrUsername, TrackNoticeCreationRequestDTO requestDTO) {

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        if (requestDTO.getTrackNoticeTitle() == null || requestDTO.getTrackNoticeTitle().trim().isEmpty() ||
                requestDTO.getTrackNoticeContent() == null || requestDTO.getTrackNoticeContent().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_NOTICE_CONTENT);
        }

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        User user = userRepository.findByEmail(userEmailOrUsername)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        TrackNotice notice = TrackNotice.builder()
                .trackNoticeTitle(requestDTO.getTrackNoticeTitle())
                .trackNoticeContent(requestDTO.getTrackNoticeContent())
                .track(track)
                .user(user)
                .build();

        TrackNotice savedNotice = trackNoticeRepository.save(notice);

        return new TrackNoticeResponseDTO(savedNotice);
    }
}

