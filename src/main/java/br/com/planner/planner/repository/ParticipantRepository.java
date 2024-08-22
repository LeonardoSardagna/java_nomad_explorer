package br.com.planner.planner.repository;

import br.com.planner.planner.domain.participant.Participant;
import br.com.planner.planner.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    List<Participant> findByTripId(UUID id);

    @Query(value = "select p.trip from Participant p where p.id =:id")
    Optional<Trip> BuscaDadosDaViagemDeAcordoComParticipnt(UUID id);
}
