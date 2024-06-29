package wercsmik.spaghetticodingclub.domain.user.service;

import java.time.LocalDate;
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
import wercsmik.spaghetticodingclub.domain.user.dto.UpdateUserNameRequestDTO;
import wercsmik.spaghetticodingclub.domain.user.dto.UpdateUserPasswordRequestDTO;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrackParticipantsRepository trackParticipantsRepository;
    private final TrackWeekRepository trackWeekRepository;
    private final AssessmentRepository assessmentRepository;

    // 사용자 본인 프로필을 조회하는 메서드
    public ProfileResponseDTO getMyProfile(UserDetailsImpl userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return createProfileResponseDTO(user);
    }

    // 특정 유저 프로필을 조회하는 메서드
    public ProfileResponseDTO getUserProfile(Long userId) {

        User user = getUser(userId);

        return createProfileResponseDTO(user);
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

    @Transactional
    public void updateUserName(Long userId, UpdateUserNameRequestDTO requestDTO, User loginUser) {
        User userToUpdate = getUser(userId);

        if (loginUser.getRole() == UserRoleEnum.USER && !loginUser.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
        }

        userToUpdate.setUsername(requestDTO.getUsername());
        userRepository.save(userToUpdate);
    }

    public User getUser(Long userId) {

        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 공통 로직: 유저의 프로필 정보를 생성하는 메서드
     * 유저의 트랙, 주차 정보 및 평가 정보를 포함한 프로필 응답 객체를 생성합니다.
     */
    private ProfileResponseDTO createProfileResponseDTO(User user) {
        List<TrackParticipants> trackParticipants = trackParticipantsRepository.findByUserUserId(user.getUserId());
        String trackName = trackParticipants.stream().findFirst()
                .map(participant -> participant.getTrack().getTrackName())
                .orElse("참여된 트랙 없음");
        Long trackId = trackParticipants.stream().findFirst()
                .map(participant -> participant.getTrack().getTrackId())
                .orElse(null);

        List<TrackWeekCreationResponseDTO> trackWeeks = trackParticipants.stream().findFirst()
                .map(participant -> trackWeekRepository.findByTrack_TrackId(participant.getTrack().getTrackId()))
                .orElse(Collections.emptyList())
                .stream()
                .map(TrackWeekCreationResponseDTO::from)
                .collect(Collectors.toList());

        Long currentTrackWeekId = trackWeeks.stream()
                .filter(trackWeek -> {
                    LocalDate now = LocalDate.now();
                    return (now.isAfter(trackWeek.getStartDate()) || now.isEqual(trackWeek.getStartDate())) &&
                            (now.isBefore(trackWeek.getEndDate()) || now.isEqual(trackWeek.getEndDate()));
                })
                .map(TrackWeekCreationResponseDTO::getTrackWeekId)
                .findFirst()
                .orElse(null);

        AssessmentResponseDTO assessment = assessmentRepository.findFirstByUserId_UserIdOrderByCreatedAtDesc(user.getUserId())
                .map(AssessmentResponseDTO::of)
                .orElse(null);

        return new ProfileResponseDTO(user, trackId, trackName, currentTrackWeekId, trackWeeks,
                assessment);
    }
}
