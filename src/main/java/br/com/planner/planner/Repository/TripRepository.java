package br.com.planner.planner.Repository;

import br.com.planner.planner.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
