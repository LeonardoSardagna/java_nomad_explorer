package br.com.planner.planner.service;

import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.domain.trip.TripResponseDTO;
import br.com.planner.planner.repository.TripRepository;
import br.com.planner.planner.validation.IValidation;
import br.com.planner.planner.validation.ValidationExistsTrip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private List<IValidation> validationList = new ArrayList<>();

    @Autowired
    private ValidationExistsTrip validationExistsTrip;

    public TripResponseDTO createTrip(@RequestBody TripRequestDTO data) {
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

        validationExistsTrip.valid(id);
        validationList.forEach(valid -> valid.valid(data));

        Trip rawTrip = trip.get();

        rawTrip.setDestination(data.destination());
        rawTrip.setStartsAt(LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
        rawTrip.setEndsAt(LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME));

        this.tripRepository.save(rawTrip);
        return rawTrip;
    }

    public Trip confirmTrip(UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);

        validationExistsTrip.valid(id);

        Trip rawTrip = trip.get();
        rawTrip.setIsConfirmed(true);
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
