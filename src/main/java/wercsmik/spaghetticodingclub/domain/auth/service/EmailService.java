package wercsmik.spaghetticodingclub.domain.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wercsmik.spaghetticodingclub.domain.auth.entity.EmailVerification;
import wercsmik.spaghetticodingclub.domain.auth.repository.EmailVerificationRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${EMAIL_USERNAME}")
    private String fromAddress;

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public void sendVerificationLink(String email, String emailType, String requesterEmail) {
        // 이메일 인증 상태 확인
        if (emailVerificationRepository.findByEmailAndIsVerifiedTrueAndEmailType(email, emailType).isPresent()) {
            log.warn("Email already verified: {}", email);
            throw new CustomException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        // 기존의 인증 기록 삭제
        emailVerificationRepository.deleteByEmailAndEmailType(email, emailType);

        // 인증 토큰 생성
        String token = UUID.randomUUID().toString();
        String verificationLink = "http://localhost:8080/api/auths/verify-email?token=" + token;

        // HTML 이메일 생성
        String messageContent = createMessageContent(emailType, requesterEmail, verificationLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject("이메일 인증 링크");
            helper.setText(messageContent, true); // true indicates the content is HTML

            mailSender.send(message);

            // 인증 토큰 저장
            EmailVerification emailVerification = EmailVerification.builder()
                    .email(email)
                    .token(token)
                    .isVerified(false)
                    .expirationDate(LocalDateTime.now().plusMinutes(30))  // 유효 시간 설정
                    .emailType(emailType)
                    .build();
            emailVerificationRepository.save(emailVerification);
            log.info("Verification link sent to: {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", email, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String createMessageContent(String emailType, String requesterEmail, String verificationLink) {
        if ("RECOMMEND_EMAIL".equals(emailType)) {
            return String.format("<p>%s 님께서 인증 요청을 위해서 링크를 보내드립니다.</p>"
                            + "<p>새로 가입하려는 관리자가 맞을 경우 아래 버튼을 클릭하세요:</p>"
                            + "<a href=\"%s\" style=\"display:inline-block;padding:10px 20px;font-size:16px;color:white;background-color:#4CAF50;text-decoration:none;border-radius:5px;\">인증하기</a>",
                    requesterEmail, verificationLink);
        } else {
            return String.format("<p>이메일 인증을 위해 아래 버튼을 클릭하세요:</p>"
                            + "<a href=\"%s\" style=\"display:inline-block;padding:10px 20px;font-size:16px;color:white;background-color:#4CAF50;text-decoration:none;border-radius:5px;\">인증하기</a>",
                    verificationLink);
        }
    }

    public boolean isEmailVerified(String email) {
        Optional<EmailVerification> verificationOpt = emailVerificationRepository.findByEmailAndIsVerifiedTrue(email);
        return verificationOpt.isPresent();
    }
}
