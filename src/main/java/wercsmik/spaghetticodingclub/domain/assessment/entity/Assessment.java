package wercsmik.spaghetticodingclub.domain.assessment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wercsmik.spaghetticodingclub.domain.user.entity.User;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User userId; // 평가 받은 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private User adminId; // 평가를 한 관리자

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssessmentType type;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;
}
