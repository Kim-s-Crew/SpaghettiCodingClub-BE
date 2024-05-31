package wercsmik.spaghetticodingclub.domain.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackWeek;

import java.util.List;

public interface TrackWeekRepository extends JpaRepository<TrackWeek, Long> {

    List<TrackWeek> findByTrack_TrackId(Long trackId);
}
