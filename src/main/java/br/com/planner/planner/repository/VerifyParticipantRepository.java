package br.com.planner.planner.repository;

import br.com.planner.planner.domain.verifyparticipant.VerifyParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerifyParticipantRepository extends JpaRepository<VerifyParticipant, UUID> {
  Optional<VerifyParticipant> findByParticipantId(UUID uuid);
}
