package wercsmik.spaghetticodingclub.domain.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.assessment.entity.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

}
