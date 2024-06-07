package wercsmik.spaghetticodingclub.domain.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wercsmik.spaghetticodingclub.domain.schedule.entity.Scheduler;
import wercsmik.spaghetticodingclub.domain.user.entity.User;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    boolean existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(User user,
            LocalDateTime endTime, LocalDateTime startTime);

    List<Scheduler> findByUserIdIn(List<User> users);

    @Query("SELECT s FROM Scheduler s WHERE s.userId IN :users AND (s.startTime >= :startDate AND s.startTime <= :endDate OR s.endTime >= :startDate AND s.endTime <= :endDate OR s.startTime < :startDate AND s.endTime > :endDate)")
    List<Scheduler> findByUserIdInAndDateRange(@Param("users") List<User> users, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) > 0 FROM Scheduler s WHERE s.userId = :user AND s.schedulerId != :schedulerId AND (s.startTime < :endTime AND s.endTime > :startTime)")
    boolean existsByUserIdAndSchedulerIdNotAndTimeRangeOverlap(@Param("user") User user, @Param("schedulerId") Long schedulerId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}