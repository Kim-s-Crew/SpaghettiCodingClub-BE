package wercsmik.spaghetticodingclub.domain.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackParticipantId;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackParticipants;

import java.util.List;

public interface TrackParticipantsRepository extends JpaRepository<TrackParticipants, TrackParticipantId> {

    List<TrackParticipants> findByTrackTrackId(Long trackId);

    List<TrackParticipants> findByUserUserId(Long userId);

    boolean existsById_UserIdAndId_TrackId(Long userId, Long trackId);
}
