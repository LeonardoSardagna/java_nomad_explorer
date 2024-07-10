package br.com.planner.planner.controller;

import br.com.planner.planner.domain.participant.Participant;
import br.com.planner.planner.domain.participant.ParticipantCreateResponse;
import br.com.planner.planner.domain.participant.ParticipantDTO;
import br.com.planner.planner.domain.participant.ParticipantDetails;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.domain.trip.TripResponseDTO;
import br.com.planner.planner.repository.TripRepository;
import br.com.planner.planner.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<TripResponseDTO> createTrip(@RequestBody TripRequestDTO data) {
        Trip newTrip = new Trip(data);
        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(data.emails_to_invite(), newTrip);
        return ResponseEntity.ok(new TripResponseDTO(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> tripDetails(@PathVariable UUID id) {
        Optional<Trip> planner = this.tripRepository.findById(id);
        return planner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //altera somente o destino e data.
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestDTO data) {
        Optional<Trip> trip = tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setDestination(data.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME));

            this.tripRepository.save(rawTrip);
            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setIsConfirmed(true);

            this.participantService.confirmationEmailParticipants(id);
            this.tripRepository.save(rawTrip);
            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrip(@PathVariable UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            tripRepository.delete(rawTrip);
            return ResponseEntity.ok("Viagem deletada com sucesso");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantDTO data){
        Optional<Trip> trip = tripRepository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            ParticipantCreateResponse participantResponse = this.participantService
                    .registerParticipantToEvent(data.email(), rawTrip);

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantDetails>> getAllParticipants(@PathVariable UUID id){
        List<ParticipantDetails> participants = this.participantService.getAllParticipantsFromEvents(id);
        return ResponseEntity.ok(participants);
    }
}
