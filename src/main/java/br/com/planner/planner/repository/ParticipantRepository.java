package br.com.planner.planner.repository;

import br.com.planner.planner.domain.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    List<Participant> findByTripId(UUID id);
}
