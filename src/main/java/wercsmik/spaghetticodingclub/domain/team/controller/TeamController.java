package wercsmik.spaghetticodingclub.domain.team.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wercsmik.spaghetticodingclub.domain.team.dto.MultipleTeamCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.team.dto.TeamCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.team.service.TeamService;
import wercsmik.spaghetticodingclub.domain.user.dto.UnassignedUserResponseDTO;
import wercsmik.spaghetticodingclub.domain.user.service.UserService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

import java.util.List;

@RestController
@RequestMapping("/tracks/{trackId}/trackWeeks/{trackWeekId}/teams")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<List<TeamCreationResponseDTO>>> createTeams(
            @PathVariable Long trackId,
            @PathVariable Long trackWeekId,
            @RequestBody MultipleTeamCreationRequestDTO requestDTO) {

        List<TeamCreationResponseDTO> createdTeams = teamService.createTeams(trackId, trackWeekId, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("팀 생성 성공", createdTeams));
    }

    @GetMapping("/users-without-team")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<List<UnassignedUserResponseDTO>>> getUsersWithoutTeam(
            @PathVariable Long trackId,
            @PathVariable Long trackWeekId) {

        List<UnassignedUserResponseDTO> users = userService.getUsersWithoutTeam(trackId, trackWeekId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of("팀이 배정되지 않은 사용자 조회 성공", users));
    }
}
