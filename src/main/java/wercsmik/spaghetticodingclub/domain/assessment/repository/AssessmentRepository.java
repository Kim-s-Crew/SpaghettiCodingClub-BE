package wercsmik.spaghetticodingclub.domain.assessment.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.assessment.entity.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    List<Assessment> findAllByUserId_UserId(Long userId);

    Optional<Assessment> findFirstByUserId_UserIdOrderByCreatedAtDesc(Long userId);
}
