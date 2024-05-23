package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackParticipantResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
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
                        participant.getUser().getId(),
                        participant.getUser().getUsername(),
                        participant.getTrack().getTrackId(),
                        participant.getTrack().getTrackName(),
                        participant.getJoinedAt()))
                .collect(Collectors.toList());
    }
}
