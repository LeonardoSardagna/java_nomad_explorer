package br.com.planner.planner.service;

import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.domain.trip.TripResponseDTO;
import br.com.planner.planner.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParticipantService participantService;

    public TripResponseDTO createTrip(@RequestBody TripRequestDTO data) {
        Trip newTrip = new Trip(data);
        LocalDateTime now = LocalDateTime.now();

        if (newTrip.getStartsAt().isBefore(now) && newTrip.getEndsAt().isBefore(now)){
            return null;
        }
        if (newTrip.getStartsAt().isAfter(newTrip.getEndsAt())){
            return null;
        }

        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(data.emails_to_invite(), newTrip);
        return new TripResponseDTO(newTrip.getId());
    }

    public Trip getTrip(UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);
        if (trip.isPresent()) {
            return trip.get();
        }
        return null;
    }

    public Trip uploudTrip(UUID id, TripRequestDTO data) {
        Optional<Trip> trip = tripRepository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setDestination(data.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            if (rawTrip.getStartsAt().isBefore(rawTrip.getEndsAt())) {
                this.tripRepository.save(rawTrip);
                return rawTrip;
            }
        }
        return null;
    }

    public Trip confirmTrip(UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);
            this.tripRepository.save(rawTrip);
            return rawTrip;
        }
        return null;
    }

    public Trip deleteTrip(UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            this.tripRepository.delete(rawTrip);
        }
        return null;
    }
}
