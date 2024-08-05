package br.com.planner.planner.service;

import br.com.planner.planner.domain.participant.ParticipantDetails;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.domain.trip.TripResponseDTO;
import br.com.planner.planner.infra.exception.InternalServerErrorHandler;
import br.com.planner.planner.infra.exception.ValidationException;
import br.com.planner.planner.repository.TripRepository;
import br.com.planner.planner.validation.IValidation;
import br.com.planner.planner.validation.ValidationData;
import br.com.planner.planner.validation.ValidationExistsTrip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private DateFormaterService dateFormaterService;

    @Autowired
    private List<IValidation> validationList = new ArrayList<>();

    @Autowired
    private ValidationData validationData;

    @Autowired
    private ValidationExistsTrip validationExistsTrip;

    public TripResponseDTO createTrip(@RequestBody TripRequestDTO data) {
        validationData.valid(data);

        Trip newTrip = new Trip(data);

        validationList.forEach(valid -> valid.valid(data));

        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(data.emails_to_invite(), newTrip);
        return new TripResponseDTO(newTrip.getId());
    }

    public Trip getTrip(UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);
        validationExistsTrip.valid(id);
        return trip.get();
    }

    public Trip uploudTrip(UUID id, TripRequestDTO data) {
        Optional<Trip> trip = tripRepository.findById(id);

        if(trip.isEmpty()){
            validationExistsTrip.valid(id);
        }
        Trip rawTrip = trip.get();

        if (data.ownerName() != null && !data.ownerName().isBlank()) {
            rawTrip.setOwnerName(data.ownerName());
        }

        if (data.ownerEmail() != null && !data.ownerEmail().isBlank()) {
            rawTrip.setOwnerEmail(data.ownerEmail());
        }

        if (data.destination() != null && !data.destination().isBlank()) {
            rawTrip.setDestination(data.destination());
        }

        if (data.starts_at() != null && !data.starts_at().isBlank()) {
            rawTrip.setStartsAt(LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
        }

        if (data.ends_at() != null && !data.ends_at().isBlank()) {
            rawTrip.setEndsAt(LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
        }

        this.tripRepository.save(rawTrip);
        return rawTrip;
    }

    public Trip confirmTrip(UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);

        List<ParticipantDetails> participants = participantService.getAllParticipantsFromEvents(id);

        validationExistsTrip.valid(id);

        Trip rawTrip = trip.get();
        rawTrip.setIsConfirmed(true);

        String startDate = dateFormaterService.dateFormater(rawTrip.getStartsAt());
        String andDate = dateFormaterService.dateFormater(rawTrip.getEndsAt());

        participants.forEach(participant -> {
            participantService.confirmationEmailParticipants(
                    participant.email(),
                    "Nomad Explorer - Convite para a Próxima Viagem!",
                    "Olá,\n" +
                            "\n" +
                            "Espero que você esteja bem!\n" +
                            "\n" +
                            rawTrip.getOwnerName() + " gostaria de convidá-lo(a) para uma viagem que está organizando através do Nomad Explorer. Seguem os detalhes:\n" +
                            "\n" +
                            "Destino: " + rawTrip.getDestination() + "\n" +
                            "Data de Início: " + startDate + "\n" +
                            "Data de Término: " + andDate + "\n" +
                            "\n" +
                            "Para confirmar sua presença e obter mais detalhes, por favor, clique no link de confirmação abaixo e insira o seu nome:\n" +
                            "\n" +
                            "http://localhost:5173/trips/" + id + "/confirm/" + participant.id() + "\n" +
                            "\n" +
                            "Estou ansioso(a) para ter você conosco nesta viagem incrível! Se tiver alguma dúvida ou precisar de mais informações, não hesite em entrar em contato.\n" +
                            "\n" +
                            "Nomad Explorer\n" +
                            "nomadexplorer@gmail.com");
        });

        this.tripRepository.save(rawTrip);
        return rawTrip;
    }

    public Trip deleteTrip(UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);
        validationExistsTrip.valid(id);
        Trip rawTrip = trip.get();
        this.tripRepository.delete(rawTrip);
        return rawTrip;
    }
}
