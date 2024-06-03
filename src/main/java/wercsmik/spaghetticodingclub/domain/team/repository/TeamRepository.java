package wercsmik.spaghetticodingclub.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
