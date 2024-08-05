package br.com.planner.planner.validation;

import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidationData implements IValidation {

    @Override
    public void valid(TripRequestDTO data) {
        if (data.ownerName() == null || data.ownerName().isBlank()) {
            throw new ValidationException("O nome do usuário host é obrigatório para a criação da viagem");
        }

        if (data.ownerEmail() == null || data.ownerEmail().isBlank()) {
            throw new ValidationException("O email do host é obrigatório para contato em caso de problemas");
        }

        if (data.destination() == null || data.destination().isBlank()) {
            throw new ValidationException("O endereço da viagem é obrigatório");
        }

        if (data.starts_at() == null) {
            throw new ValidationException("A data de início da viagem é obrigatória");
        }

        if (data.ends_at() == null) {
            throw new ValidationException("A data de fim da viagem é obrigatória");
        }
    }
}
