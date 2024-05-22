package wercsmik.spaghetticodingclub.domain.track.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;

@Entity
@Getter
@RequiredArgsConstructor
public class Track extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String trackName;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
}