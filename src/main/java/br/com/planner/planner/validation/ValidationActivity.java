package br.com.planner.planner.validation;

import br.com.planner.planner.domain.trip.TripRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidationActivity implements IValidation{
    @Override
    public void valid(TripRequestDTO data) {

    }
}
