package wercsmik.spaghetticodingclub.domain.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.track.entity.Track;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {

    boolean existsByTrackName(String trackName);

    Optional<Track> findByTrackName(String trackName);


}
