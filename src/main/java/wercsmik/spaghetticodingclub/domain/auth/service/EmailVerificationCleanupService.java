package wercsmik.spaghetticodingclub.domain.auth.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wercsmik.spaghetticodingclub.domain.auth.repository.EmailVerificationRepository;

@Service
@RequiredArgsConstructor
public class EmailVerificationCleanupService {

    private final EmailVerificationRepository emailVerificationRepository;

    @Scheduled(fixedRate = 3600000)  // 1시간마다 실행
    public void cleanupExpiredVerifications() {

        LocalDateTime now = LocalDateTime.now();
        emailVerificationRepository.deleteByExpirationDateBefore(now);

        System.out.println("Expired email verifications cleaned up at: " + now);
    }

}
