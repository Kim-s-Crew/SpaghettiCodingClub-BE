package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackWeekCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackWeekCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackWeek;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackWeekRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TrackWeekService {

    private final TrackWeekRepository trackWeekRepository;
    private final TrackRepository trackRepository;

    @Transactional
    public TrackWeekCreationResponseDTO createTrackWeek(Long trackId, TrackWeekCreationRequestDTO requestDTO) {

        if (!isAdmin()) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        validateTrackWeekDates(trackId, requestDTO.getStartDate(), requestDTO.getEndDate());

        TrackWeek trackWeek = TrackWeek.builder()
                .track(track)
                .weekName(requestDTO.getWeekName())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .build();

        TrackWeek savedTrackWeek = trackWeekRepository.save(trackWeek);

        return convertToDto(savedTrackWeek);
    }

    /*
        공통 로직을 메서드로 분리
     */

    private TrackWeekCreationResponseDTO convertToDto(TrackWeek trackWeek) {

        return TrackWeekCreationResponseDTO.builder()
                .trackWeekId(trackWeek.getTrackWeekId())
                .weekName(trackWeek.getWeekName())
                .startDate(trackWeek.getStartDate())
                .endDate(trackWeek.getEndDate())
                .build();
    }

    private void validateTrackWeekDates(Long trackId, LocalDate startDate, LocalDate endDate) {

        List<TrackWeek> existingWeeks = trackWeekRepository.findByTrack_TrackId(trackId);
        for (TrackWeek week : existingWeeks) {
            if (week.getStartDate().isBefore(endDate) && week.getEndDate().isAfter(startDate)) {
                throw new CustomException(ErrorCode.TRACK_WEEK_OVERLAP);
            }
        }
    }

    private boolean isAdmin() {

        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
