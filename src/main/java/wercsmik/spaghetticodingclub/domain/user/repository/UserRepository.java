package wercsmik.spaghetticodingclub.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.userId NOT IN (SELECT tm.user.userId FROM TeamMember tm)")
    List<User> findUnassignedUsersByRole(@Param("role") UserRoleEnum role);

    @Query("SELECT u FROM User u WHERE u.userId IN " +
            "(SELECT tp.user.userId FROM TrackParticipants tp WHERE tp.track.trackId = :trackId) " +
            "AND u.userId NOT IN " +
            "(SELECT tm.user.userId FROM TeamMember tm WHERE tm.team.trackWeek.trackWeekId = :weekId)")
    List<User> findUsersWithoutTeam(@Param("trackId") Long trackId, @Param("weekId") Long weekId);
}
