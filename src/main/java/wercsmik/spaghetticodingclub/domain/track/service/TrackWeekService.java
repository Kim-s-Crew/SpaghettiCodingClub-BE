package wercsmik.spaghetticodingclub.domain.track.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.team.dto.TeamDetailResponseDTO;
import wercsmik.spaghetticodingclub.domain.team.dto.TeamMemberDetailResponseDTO;
import wercsmik.spaghetticodingclub.domain.team.entity.Team;
import wercsmik.spaghetticodingclub.domain.team.repository.TeamMemberRepository;
import wercsmik.spaghetticodingclub.domain.track.dto.*;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackWeek;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackWeekRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrackWeekService {

    private final TrackWeekRepository trackWeekRepository;
    private final TrackRepository trackRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public TrackWeekCreationResponseDTO createTrackWeek(Long trackId, TrackWeekCreationRequestDTO requestDTO) {

        if (!isAdmin()) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        validateDateRange(requestDTO.getStartDate(), requestDTO.getEndDate());
        validateTrackWeekDates(trackId, requestDTO.getStartDate(), requestDTO.getEndDate());

        TrackWeek trackWeek = TrackWeek.builder()
                .track(track)
                .weekName(requestDTO.getWeekName())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .build();

        trackWeekRepository.save(trackWeek);

        return convertToCreationDTO(trackWeek);
    }

    @Transactional(readOnly = true)
    public TrackWeekDetailResponseDTO getTrackWeekDetail(Long trackId, Long trackWeekId) {

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        TrackWeek trackWeek = trackWeekRepository.findById(trackWeekId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_WEEK_NOT_FOUND));

        List<TeamDetailResponseDTO> teamDetails = trackWeek.getTeams().stream()
                .map(this::mapToTeamDetailDTO)
                .collect(Collectors.toList());

        return TrackWeekDetailResponseDTO.builder()
                .trackWeekId(trackWeek.getTrackWeekId())
                .weekName(trackWeek.getWeekName())
                .startDate(trackWeek.getStartDate())
                .endDate(trackWeek.getEndDate())
                .teams(teamDetails)
                .build();
    }

    @Transactional(readOnly = true)
    public List<TrackWeekListResponseDTO> findAllTrackWeeksByTrackId(Long trackId) {

        List<TrackWeek> trackWeeks = trackWeekRepository.findByTrack_TrackId(trackId);

        return trackWeeks.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TrackWeekUpdateResponseDTO updateTrackWeek(Long trackId, Long weekId, TrackWeekUpdateRequestDTO requestDTO) {

        if (!isAdmin()) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        TrackWeek trackWeek = trackWeekRepository.findById(weekId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_WEEK_NOT_FOUND));

        validateUpdates(trackWeek, requestDTO);

        trackWeekRepository.save(trackWeek);

        return new TrackWeekUpdateResponseDTO.Builder()
                .trackWeekId(trackWeek.getTrackWeekId())
                .weekName(trackWeek.getWeekName())
                .startDate(trackWeek.getStartDate())
                .endDate(trackWeek.getEndDate())
                .build();
    }

    /*
        공통 로직을 메서드로 분리
     */
    private void validateUpdates(TrackWeek trackWeek, TrackWeekUpdateRequestDTO requestDTO) {
        Optional.ofNullable(requestDTO.getWeekName()).ifPresent(trackWeek::setWeekName);
        Optional.ofNullable(requestDTO.getStartDate()).ifPresent(startDate -> {
            validateDateRange(startDate, trackWeek.getEndDate());
            trackWeek.setStartDate(startDate);
        });
        Optional.ofNullable(requestDTO.getEndDate()).ifPresent(endDate -> {
            validateDateRange(trackWeek.getStartDate(), endDate);
            trackWeek.setEndDate(endDate);
        });
    }

    private TrackWeekCreationResponseDTO convertToCreationDTO(TrackWeek trackWeek) {

        return TrackWeekCreationResponseDTO.builder()
                .trackWeekId(trackWeek.getTrackWeekId())
                .weekName(trackWeek.getWeekName())
                .startDate(trackWeek.getStartDate())
                .endDate(trackWeek.getEndDate())
                .build();
    }

    private TrackWeekListResponseDTO convertToListDTO(TrackWeek trackWeek) {

        return TrackWeekListResponseDTO.builder()
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

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.START_DATE_BEFORE_CURRENT);
        }
        if (endDate.isBefore(startDate)) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

    private TeamDetailResponseDTO mapToTeamDetailDTO(Team team) {
        List<TeamMemberDetailResponseDTO> memberDetails = teamMemberRepository.findByTeam_TeamId(team.getTeamId()).stream()
                .map(teamMember -> new TeamMemberDetailResponseDTO(
                        teamMember.getUser().getUserId(),
                        teamMember.getUser().getUsername()
                ))
                .collect(Collectors.toList());

        return new TeamDetailResponseDTO(
                team.getTeamId(),
                team.getTeamName(),
                memberDetails
        );
    }


}
