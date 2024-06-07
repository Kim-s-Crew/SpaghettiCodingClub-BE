package wercsmik.spaghetticodingclub.domain.unlike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.unlike.entity.Unlike;

public interface UnlikeRepository extends JpaRepository<Unlike, Long> {
}
