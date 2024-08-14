package wercsmik.spaghetticodingclub.domain.auth.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.auth.entity.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByToken(String token);

    Optional<EmailVerification> findByEmail(String email);

    Optional<EmailVerification> findByEmailAndIsVerifiedTrue(String email);

    Optional<EmailVerification> findByEmailAndIsVerifiedTrueAndEmailType(String email, String emailType);

    void deleteByEmailAndEmailType(String email, String emailType);

    void deleteByEmail(String email);

    void deleteByExpirationDateBefore(LocalDateTime now);  // 만료된 인증 정보를 삭제하는 메서드
}