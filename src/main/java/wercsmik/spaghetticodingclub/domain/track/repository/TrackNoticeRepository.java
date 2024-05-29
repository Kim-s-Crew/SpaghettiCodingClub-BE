package wercsmik.spaghetticodingclub.domain.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackNotice;

import java.util.List;
import java.util.Set;

public interface TrackNoticeRepository extends JpaRepository<TrackNotice, Long> {

    List<TrackNotice> findAllByTrack_TrackIdIn(Set<Long> trackIds);

}
