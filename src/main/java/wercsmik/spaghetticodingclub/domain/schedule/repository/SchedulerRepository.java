package wercsmik.spaghetticodingclub.domain.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.schedule.entity.Scheduler;
import wercsmik.spaghetticodingclub.domain.user.entity.User;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    boolean existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(User user,
            LocalDateTime endTime, LocalDateTime startTime);

    List<Scheduler> findByUserIdIn(List<User> users);
}