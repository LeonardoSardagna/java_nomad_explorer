package br.com.planner.planner.repository;

import br.com.planner.planner.domain.activity.Activity;
import br.com.planner.planner.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> findByTripId(UUID id);

    List<Activity> findByTripAndOccursAtBetween(Trip trip, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
