package wercsmik.spaghetticodingclub.domain.assessment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Assessment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User userId; // 평가 받은 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private User adminId; // 평가를 한 관리자

    @Column
    private String background;

    @Column
    private String guidance;

    @Column
    private String relationship;
}
