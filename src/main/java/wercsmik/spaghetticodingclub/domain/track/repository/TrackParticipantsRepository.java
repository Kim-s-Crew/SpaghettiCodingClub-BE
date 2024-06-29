package wercsmik.spaghetticodingclub.domain.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackParticipants;

import java.util.List;
import java.util.Optional;

public interface TrackParticipantsRepository extends JpaRepository<TrackParticipants, Long> {

    List<TrackParticipants> findByTrackTrackId(Long trackId);

    @Query("SELECT tp FROM TrackParticipants tp JOIN FETCH tp.track t WHERE tp.user.userId = :userId")
    List<TrackParticipants> findByUserUserId(Long userId);

    Optional<TrackParticipants> findByUser_UserIdAndTrack_TrackId(Long userId, Long trackId);
}
