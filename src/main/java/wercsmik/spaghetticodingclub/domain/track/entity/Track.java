package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Track extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;

    @Column(nullable = false, length = 50)
    private String trackName;

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
}