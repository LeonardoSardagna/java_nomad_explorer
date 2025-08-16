package br.com.planner.planner.repository;

import br.com.planner.planner.domain.participant.Participant;
import br.com.planner.planner.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByTripId(Long id);

    @Query(value = "select p.trip from Participant p where p.id =:id")
    Optional<Trip> BuscaDadosDaViagemDeAcordoComParticipnt(Long id);

    Optional<Participant> findById(Long id);

    void deleteById(Long id);
}
