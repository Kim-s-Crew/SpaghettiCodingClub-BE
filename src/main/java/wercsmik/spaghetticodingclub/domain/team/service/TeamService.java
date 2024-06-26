package wercsmik.spaghetticodingclub.domain.team.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.team.dto.MultipleTeamCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.team.dto.TeamCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.team.entity.Team;
import wercsmik.spaghetticodingclub.domain.team.entity.TeamMember;
import wercsmik.spaghetticodingclub.domain.team.repository.TeamMemberRepository;
import wercsmik.spaghetticodingclub.domain.team.repository.TeamRepository;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackWeek;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackWeekRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final TrackWeekRepository trackWeekRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public List<TeamCreationResponseDTO> createTeams(Long trackId, Long trackWeekId, MultipleTeamCreationRequestDTO requestDTO) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));

        TrackWeek trackWeek = trackWeekRepository.findById(trackWeekId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRACK_WEEK_NOT_FOUND));

        if (!isAdmin()) {
            throw new CustomException(ErrorCode.NO_AUTHENTICATION);
        }

        return requestDTO.getTeams().stream().map(teamRequest -> {
            List<Long> memberIds = teamRequest.getMemberIds();
            List<User> members = userRepository.findAllById(memberIds);

            if (members.size() != memberIds.size()) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }

            Team team = Team.builder()
                    .trackWeek(trackWeek)
                    .teamName(teamRequest.getTeamName())
                    .build();

            final Team savedTeam = teamRepository.save(team);

            List<TeamMember> teamMembers = members.stream().map(member ->
                    TeamMember.builder()
                            .team(savedTeam)
                            .user(member)
                            .joinedAt(LocalDateTime.now())
                            .build()
            ).collect(Collectors.toList());

            teamMemberRepository.saveAll(teamMembers);

            List<Map<String, Object>> memberDetails = teamMembers.stream()
                    .map(tm -> Map.<String, Object>of(
                            "userId", tm.getUser().getUserId(),
                            "username", tm.getUser().getUsername()
                    )).collect(Collectors.toList());

            return new TeamCreationResponseDTO(savedTeam.getTeamId(), savedTeam.getTeamName(), memberDetails);
        }).collect(Collectors.toList());
    }


    /*
        공통 로직을 메서드로 분리
     */

    private boolean isAdmin() {

        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
