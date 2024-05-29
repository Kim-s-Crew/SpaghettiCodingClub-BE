package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.*;
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
public class TrackNotice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(nullable = false, length = 200)
    private String trackNoticeTitle;

    @Column(nullable = false)
    private String trackNoticeContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trackId", nullable = false)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
