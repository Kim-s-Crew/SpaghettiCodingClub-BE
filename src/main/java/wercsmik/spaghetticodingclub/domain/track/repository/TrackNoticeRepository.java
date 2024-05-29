package wercsmik.spaghetticodingclub.domain.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackNotice;

public interface TrackNoticeRepository extends JpaRepository<TrackNotice, Long> {
}
