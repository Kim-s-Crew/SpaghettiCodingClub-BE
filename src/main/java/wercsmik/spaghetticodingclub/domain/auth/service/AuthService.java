package wercsmik.spaghetticodingclub.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wercsmik.spaghetticodingclub.domain.auth.dto.SignRequestDTO;
import wercsmik.spaghetticodingclub.domain.track.service.TrackParticipantsService;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrackParticipantsService trackParticipantsService;

    public void signup(SignRequestDTO signRequestDTO) {

        String username = signRequestDTO.getUsername();
        String email = signRequestDTO.getEmail();
        String password = signRequestDTO.getPassword();
        String checkPassword = signRequestDTO.getCheckPassword();
        String track = signRequestDTO.getTrack();
        String recommendEmail = signRequestDTO.getRecommendEmail();

        // 이메일 중복확인
        if (userRepository.findByEmail(signRequestDTO.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
        }

        // 비밀번호 != 비밀번호 확인
        if (!Objects.equals(password, checkPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_CONFIRMATION_NOT_MATCH);
        }

        String encodePassword = passwordEncoder.encode(password);

        UserRoleEnum role = UserRoleEnum.USER; // 기본적으로 USER로 설정
        // recommendEmail 값이 null이 아니고 빈 문자열이 아닐 경우에만 ADMIN 설정
        if (recommendEmail != null && !recommendEmail.trim().isEmpty()) {
            role = UserRoleEnum.ADMIN;
        }

        // USER로 가입하고 트랙이 지정되어 있을 경우, 트랙 존재 여부 먼저 확인
        if (role == UserRoleEnum.USER && trackName != null && !trackName.trim().isEmpty()) {
            trackRepository.findByTrackName(trackName)
                    .orElseThrow(() -> new CustomException(ErrorCode.TRACK_NOT_FOUND));
            // 여기서 예외가 발생하면, 아래의 유저 저장 로직은 실행되지 않음
        }

        User user = User.builder()
                .username(username)
                .password(encodePassword)
                .email(email)
                .track(track)
                .recommendEmail(recommendEmail)
                .role(role)
                .build();
        userRepository.save(user);

        // recommendEmail이 없어서 USER로 가입한 경우에만 트랙참여자에 추가
        if (role == UserRoleEnum.USER) {
            trackParticipantsService.addParticipant(user.getUserId(), user.getTrack());
        }
    }
}
