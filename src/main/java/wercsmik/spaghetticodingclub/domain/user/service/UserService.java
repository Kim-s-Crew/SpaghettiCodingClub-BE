package wercsmik.spaghetticodingclub.domain.user.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.assessment.dto.AssessmentResponseDTO;
import wercsmik.spaghetticodingclub.domain.assessment.repository.AssessmentRepository;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackWeekCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackParticipants;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackParticipantsRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackWeekRepository;
import wercsmik.spaghetticodingclub.domain.user.dto.ProfileResponseDTO;
import wercsmik.spaghetticodingclub.domain.user.dto.UpdateUserPasswordRequestDTO;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrackParticipantsRepository trackParticipantsRepository;
    private final TrackWeekRepository trackWeekRepository;
    private final AssessmentRepository assessmentRepository;

    public ProfileResponseDTO getProfile(Long userId) {

        User user = getUser(userId);

        // 사용자가 참여하고 있는 트랙 조회
        List<TrackParticipants> trackParticipants = trackParticipantsRepository.findByUserUserId(user.getUserId());
        String trackName = trackParticipants.stream().findFirst()
                .map(participant -> participant.getTrack().getTrackName())
                .orElse("참여된 트랙 없음"); // 사용자가 어떤 트랙에도 참여하지 않았다면 기본값 설정

        // 해당 트랙의 주차 정보 조회
        List<TrackWeekCreationResponseDTO> trackWeeks = trackParticipants.stream().findFirst()
                .map(participant -> trackWeekRepository.findByTrack_TrackId(participant.getTrack().getTrackId()))
                .orElse(Collections.emptyList())
                .stream()
                .map(TrackWeekCreationResponseDTO::from)
                .collect(Collectors.toList());

        // 유저에 대한 평가 정보 조회
        List<AssessmentResponseDTO> assessments = assessmentRepository.findAllByUserId_UserId(user.getUserId())
                .stream()
                .map(AssessmentResponseDTO::of)
                .toList();


        return new ProfileResponseDTO(user, trackName, trackWeeks, assessments);
    }

    public User getUser(Long userId) {

        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updatePassword(Long userId, UpdateUserPasswordRequestDTO requestDTO, User loginUser) {

        String password = requestDTO.getPassword();
        String updatePassword = requestDTO.getUpdatePassword();
        String checkUpdatePassword = requestDTO.getCheckUpdatePassword();

        User user = getUser(userId);

        if (!loginUser.getUsername().equals(user.getUsername())) {
            throw new CustomException(ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
        }

        if (!updatePassword.equals(checkUpdatePassword)) {
            throw new CustomException(ErrorCode.PASSWORD_CONFIRMATION_EXCEPTION);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION);
        } else {
            updatePassword = passwordEncoder.encode(updatePassword);
            user.setPassword(updatePassword);
        }

        userRepository.save(user);
    }
}
