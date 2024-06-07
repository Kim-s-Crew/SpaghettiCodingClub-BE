package wercsmik.spaghetticodingclub.domain.unlike.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wercsmik.spaghetticodingclub.domain.team.entity.TeamMember;
import wercsmik.spaghetticodingclub.domain.team.repository.TeamMemberRepository;
import wercsmik.spaghetticodingclub.domain.unlike.dto.UnlikeCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.unlike.dto.UnlikeCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.unlike.entity.Unlike;
import wercsmik.spaghetticodingclub.domain.unlike.repository.UnlikeRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UnlikeService {

    private final UnlikeRepository unlikeRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public UnlikeCreationResponseDTO createUnlike(UnlikeCreationRequestDTO unlikeRequestDto) {
        validateTeamMembers(unlikeRequestDto.getSenderUserId(), unlikeRequestDto.getReceiverUserId(), unlikeRequestDto.getTeamId());

        User sender = userRepository.findById(unlikeRequestDto.getSenderUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(unlikeRequestDto.getReceiverUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();

        Unlike unlike = Unlike.builder()
                .sender(sender)
                .receiver(receiver)
                .team(teamMemberRepository.findByTeam_TeamId(unlikeRequestDto.getTeamId()).stream()
                        .findFirst()
                        .map(TeamMember::getTeam)
                        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND)))
                .cause(unlikeRequestDto.getCause())
                .createdAt(now)
                .build();

        unlikeRepository.save(unlike);

        return new UnlikeCreationResponseDTO(unlike.getUnlikeId(), sender.getUserId(), receiver.getUserId(), unlikeRequestDto.getTeamId(), unlike.getCause());
    }

    /*
        공통 로직을
        메서드로 분리
     */

    private void validateTeamMembers(Long senderUserId, Long receiverUserId, Long teamId) {
        List<Long> memberUserIds = teamMemberRepository.findByTeam_TeamId(teamId)
                .stream()
                .map(tm -> tm.getUser().getUserId())
                .toList();

        if (!memberUserIds.contains(senderUserId) || !memberUserIds.contains(receiverUserId)) {
            throw new CustomException(ErrorCode.TEAM_MEMBERS_NOT_FOUND);
        }
    }
}
