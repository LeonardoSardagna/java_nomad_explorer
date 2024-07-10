package br.com.planner.planner.repository;

import br.com.planner.planner.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
