package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TrackWeeks")
public class TrackWeek extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackWeekId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trackId", nullable = false)
    private Track track;

    @Column(nullable = false, length = 50)
    private String weekName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;
}
