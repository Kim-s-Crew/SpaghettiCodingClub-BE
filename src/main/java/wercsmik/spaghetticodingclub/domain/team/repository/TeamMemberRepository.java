package wercsmik.spaghetticodingclub.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wercsmik.spaghetticodingclub.domain.team.entity.TeamMember;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findByTeam_TeamId(Long teamId);
}
