package br.com.planner.planner.validation;

import br.com.planner.planner.domain.trip.TripRequestDTO;

public interface IValidation {
    void valid(TripRequestDTO data);
}
