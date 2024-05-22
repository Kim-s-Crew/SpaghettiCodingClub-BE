package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    public List<TrackResponseDTO> getAllTracks() {

        List<Track> tracks = trackRepository.findAll();

        return tracks.stream()
                .map(track -> new TrackResponseDTO(track.getTrackId(), track.getTrackName()))
                .collect(Collectors.toList());
    }
}
