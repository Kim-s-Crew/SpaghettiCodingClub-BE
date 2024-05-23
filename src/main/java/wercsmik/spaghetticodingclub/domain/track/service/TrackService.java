package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    @Transactional
    public TrackResponseDTO createTrack(TrackRequestDTO trackRequestDTO) {

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        if (trackRequestDTO.getTrackName() == null || trackRequestDTO.getTrackName().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TRACK_NAME);
        }

        if (trackRepository.existsByTrackName(trackRequestDTO.getTrackName())) {
            throw new CustomException(ErrorCode.TRACK_NAME_DUPLICATED);
        }

        Track track = Track.builder()
                .trackName(trackRequestDTO.getTrackName())
                .build();

        Track savedTrack = trackRepository.save(track);

        return new TrackResponseDTO(savedTrack.getTrackId(), savedTrack.getTrackName());
    }

    public List<TrackResponseDTO> getAllTracks() {

        List<Track> tracks = trackRepository.findAll();

        return tracks.stream()
                .map(track -> new TrackResponseDTO(track.getTrackId(), track.getTrackName()))
                .collect(Collectors.toList());
    }
}
