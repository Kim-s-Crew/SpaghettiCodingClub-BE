package wercsmik.spaghetticodingclub.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column(nullable = false)
    private LocalDateTime expirationDate;  // 유효 시간 추가

    @Column(nullable = false)
    private String emailType;  // 이메일 유형 추가 (USER_EMAIL, RECOMMEND_EMAIL)

    @Builder
    public EmailVerification(String email, String token, boolean isVerified, LocalDateTime expirationDate, String emailType) {
        this.email = email;
        this.token = token;
        this.isVerified = isVerified;
        this.expirationDate = expirationDate;
        this.emailType = emailType;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
}