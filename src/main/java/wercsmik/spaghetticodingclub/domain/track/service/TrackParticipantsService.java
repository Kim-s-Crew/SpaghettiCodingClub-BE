package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackParticipantResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackParticipants;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackParticipantsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackParticipantsService {

    private final TrackParticipantsRepository trackParticipantsRepository;

    public List<TrackParticipantResponseDTO> getParticipantsByTrack(Long trackId) {
        List<TrackParticipants> participants = trackParticipantsRepository.findByTrackId(trackId);
        return participants.stream()
                .map(participant -> new TrackParticipantResponseDTO(
                        participant.getUser().getId(),
                        participant.getUser().getUsername(),
                        participant.getTrack().getTrackId(),
                        participant.getJoinedAt()))
                .collect(Collectors.toList());
    }
}
