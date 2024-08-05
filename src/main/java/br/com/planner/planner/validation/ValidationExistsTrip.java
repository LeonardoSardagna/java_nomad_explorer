package br.com.planner.planner.validation;

import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.infra.exception.ValidationNotFoundException;
import br.com.planner.planner.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ValidationExistsTrip {

    @Autowired
    private TripRepository tripRepository;

    public void valid(UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);
        if (trip.isEmpty()) {
            throw new ValidationNotFoundException("Viagem não encontrada");
        }
    }

}
