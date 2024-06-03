package wercsmik.spaghetticodingclub.domain.schedule.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.schedule.entity.Scheduler;
import wercsmik.spaghetticodingclub.domain.user.entity.User;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    boolean existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(User user, LocalDateTime endTime, LocalDateTime startTime);
}
