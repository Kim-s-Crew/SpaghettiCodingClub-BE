package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @Setter
    @Column(nullable = false, length = 200)
    private String trackNoticeTitle;

    @Setter
    @Column(nullable = false)
    private String trackNoticeContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trackId", referencedColumnName = "trackId", nullable = false)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
