package wercsmik.spaghetticodingclub.domain.auth.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.auth.dto.DeleteRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.dto.SignRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.entity.EmailVerification;
import wercsmik.spaghetticodingclub.domain.auth.repository.EmailVerificationRepository;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackRepository;
import wercsmik.spaghetticodingclub.domain.track.service.TrackParticipantsService;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrackParticipantsService trackParticipantsService;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public void signup(SignRequestDTO signRequestDTO) {
        String username = signRequestDTO.getUsername();
        String email = signRequestDTO.getEmail();
        String password = signRequestDTO.getPassword();
        String checkPassword = signRequestDTO.getCheckPassword();
        String trackName = signRequestDTO.getTrack();
        String recommendEmail = signRequestDTO.getRecommendEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Email already exists: {}", email);
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXIST);
        }

        if (!Objects.equals(password, checkPassword)) {
            log.warn("Passwords do not match for: {}", email);
            throw new CustomException(ErrorCode.PASSWORD_CONFIRMATION_NOT_MATCH);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (recommendEmail != null && !recommendEmail.trim().isEmpty()) {
            role = UserRoleEnum.ADMIN;
        }

        if (role == UserRoleEnum.USER && (trackName == null || trackName.trim().isEmpty())) {
            log.warn("Track is required for: {}", email);
            throw new CustomException(ErrorCode.TRACK_REQUIRED);
        }

        if (role == UserRoleEnum.USER) {
            trackRepository.findByTrackName(trackName)
                    .orElseThrow(() -> {
                        log.warn("Track not found: {}", trackName);
                        return new CustomException(ErrorCode.TRACK_NOT_FOUND);
                    });
        }

        // 이메일 인증 여부 확인
        if (role == UserRoleEnum.ADMIN) {
            boolean isUserEmailVerified = emailVerificationRepository.findByEmailAndIsVerifiedTrueAndEmailType(email, "USER_EMAIL").isPresent();
            boolean isRecommendEmailVerified = emailVerificationRepository.findByEmailAndIsVerifiedTrueAndEmailType(recommendEmail, "RECOMMEND_EMAIL").isPresent();

            if (!isUserEmailVerified || !isRecommendEmailVerified) {
                log.warn("Email verification incomplete for: {}", email);
                throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
            }
        } else {
            EmailVerification emailVerification = emailVerificationRepository.findByEmailAndIsVerifiedTrue(email)
                    .orElseThrow(() -> {
                        log.warn("Email not verified: {}", email);
                        return new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
                    });

            if (emailVerification.getExpirationDate().isBefore(LocalDateTime.now())) {
                log.warn("Verification token expired for: {}", email);
                throw new CustomException(ErrorCode.INVALID_VERIFICATION_TOKEN);
            }
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .recommendEmail(recommendEmail)
                .role(role)
                .isVerified(true)
                .build();

        userRepository.save(user);

        if (role == UserRoleEnum.USER && trackName != null) {
            trackParticipantsService.addParticipant(user.getUserId(), trackName);
        }

        if (role == UserRoleEnum.ADMIN) {
            emailVerificationRepository.deleteByEmailAndEmailType(email, "USER_EMAIL"); // 인증 후 삭제
            emailVerificationRepository.deleteByEmailAndEmailType(recommendEmail, "RECOMMEND_EMAIL"); // 인증 후 삭제
        } else {
            emailVerificationRepository.deleteByEmail(email); // 인증 후 삭제
        }
    }

    @Transactional
    public boolean verifyEmail(String token) {
        System.out.println("Verifying email with token: " + token);
        Optional<EmailVerification> verificationOpt = emailVerificationRepository.findByToken(token);
        if (verificationOpt.isPresent()) {
            EmailVerification emailVerification = verificationOpt.get();
            if (emailVerification.getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new CustomException(ErrorCode.INVALID_VERIFICATION_TOKEN);
            }
            emailVerification.setVerified(true);
            emailVerificationRepository.save(emailVerification);
            return true;
        } else {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_TOKEN);
        }
    }

    public boolean isEmailVerified(String email) {
        Optional<EmailVerification> emailVerificationOpt = emailVerificationRepository.findByEmailAndIsVerifiedTrue(email);
        boolean isVerified = emailVerificationOpt.isPresent();

        return isVerified;
    }

    @Transactional
    public void withDrawUser(UserDetailsImpl userDetails, DeleteRequestDTO deleteRequestDTO) {
        Long userId = userDetails.getUser().getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 확인
        if (!passwordEncoder.matches(deleteRequestDTO.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION);
        }

        // 트랙 참여자 목록에서 제거
        trackParticipantsService.removeParticipant(userId);

        // 유저 삭제
        userRepository.delete(user);
    }
}
