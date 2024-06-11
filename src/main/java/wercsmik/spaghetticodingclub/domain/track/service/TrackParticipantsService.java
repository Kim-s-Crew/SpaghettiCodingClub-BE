package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackParticipantResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackParticipantUpdateResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackParticipantId;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackParticipants;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackParticipantsRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackParticipantsService {

    private final TrackParticipantsRepository trackParticipantsRepository;

    private final UserRepository userRepository;

    private final TrackRepository trackRepository;

    public void addParticipant(Long userId, String trackName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Track track = trackRepository.findByTrackName(trackName)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        TrackParticipants participant = TrackParticipants.builder()
                .user(user)
                .track(track)
                .joinedAt(LocalDateTime.now())
                .build();

        trackParticipantsRepository.save(participant);
    }

    public List<TrackParticipantResponseDTO> getParticipantsByTrack(Long trackId) {

        List<TrackParticipants> participants = trackParticipantsRepository.findByTrackTrackId(trackId);

        return participants.stream()
                .map(participant -> new TrackParticipantResponseDTO(
                        participant.getUser().getUserId(),
                        participant.getUser().getUsername(),
                        participant.getTrack().getTrackId(),
                        participant.getTrack().getTrackName(),
                        participant.getJoinedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public TrackParticipantUpdateResponseDTO updateParticipantTrack(Long userId, Long oldTrackId, Long newTrackId) {
        // 관리자 권한 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        // 새 트랙의 존재 여부 확인
        Track newTrack = trackRepository.findById(newTrackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        // 트랙 참여자 정보 조회
        TrackParticipantId participantId = new TrackParticipantId(userId, oldTrackId);
        TrackParticipants participant = trackParticipantsRepository.findById(participantId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기존 트랙 참여자 정보 삭제
        trackParticipantsRepository.delete(participant);

        // 새로운 트랙 참여자 정보 생성
        TrackParticipants newParticipant = TrackParticipants.builder()
                .user(participant.getUser())
                .track(newTrack)
                .joinedAt(LocalDateTime.now())
                .build();

        trackParticipantsRepository.save(newParticipant);

        return TrackParticipantUpdateResponseDTO.builder()
                .userId(userId)
                .updatedTrackName(newTrack.getTrackName())
                .build();
    }

    @Transactional
    public void removeParticipant(Long userId) {
        List<TrackParticipants> participants = trackParticipantsRepository.findByUserUserId(userId);
        trackParticipantsRepository.deleteAll(participants);
    }
}
