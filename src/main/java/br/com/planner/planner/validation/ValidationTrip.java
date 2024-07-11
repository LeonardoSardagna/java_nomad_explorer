package br.com.planner.planner.validation;

import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidationTrip implements IValidation {

    @Override
    public void valid(TripRequestDTO data) {
        Trip newTrip = new Trip(data);
        LocalDateTime now = LocalDateTime.now();

        if (newTrip.getStartsAt().isBefore(now) && newTrip.getEndsAt().isBefore(now)) {
            throw new ValidationException("A viagem não pode ser agendada no passado.");
        }
        if (newTrip.getStartsAt().isAfter(newTrip.getEndsAt())) {
            throw new ValidationException("A data de início da viagem não pode ser após a data de término.");
        }
    }
}
