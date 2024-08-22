package br.com.planner.planner.controller;

import br.com.planner.planner.domain.activity.*;
import br.com.planner.planner.domain.link.LinkDetails;
import br.com.planner.planner.domain.link.LinkRequestDTO;
import br.com.planner.planner.domain.link.LinkResponseDTO;
import br.com.planner.planner.domain.participant.ParticipantCreateResponse;
import br.com.planner.planner.domain.participant.ParticipantDetails;
import br.com.planner.planner.domain.participant.ParticipantRequestDTO;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.trip.TripRequestDTO;
import br.com.planner.planner.domain.trip.TripResponseDTO;
import br.com.planner.planner.service.ActivityService;
import br.com.planner.planner.service.LinkService;
import br.com.planner.planner.service.ParticipantService;
import br.com.planner.planner.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private TripService tripService;

    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity<TripResponseDTO> createTrip(@RequestBody TripRequestDTO data) {
        TripResponseDTO tripResponseDTO = this.tripService.createTrip(data);
        return ResponseEntity.ok(tripResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> tripDetails(@PathVariable UUID id) throws Exception {
        Trip trip = this.tripService.getTrip(id);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestDTO data) throws Exception {
        Trip trip = this.tripService.uploudTrip(id, data);
        return ResponseEntity.ok(trip);
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) throws Exception {
        Trip trip = this.tripService.confirmTrip(id);
        return ResponseEntity.ok(trip);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrip(@PathVariable UUID id) throws Exception {
        this.tripService.deleteTrip(id);
        return ResponseEntity.ok("A viagem foi deletada com sucesso");
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestDTO data){
        Trip trip = tripService.getTrip(id);

        ParticipantCreateResponse participantResponse = this.participantService
                .registerParticipantToEvent(data.email(), trip);
        return ResponseEntity.ok(participantResponse);
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantDetails>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantDetails> participants = this.participantService.getAllParticipantsFromEvents(id);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponseDTO> createActivity(@PathVariable UUID id, @RequestBody @Valid ActivityRequestDTO data) {
        Trip trip = this.tripService.getTrip(id);
        ActivityResponseDTO activityResponse = this.activityService.registerActivity(trip, data);
        return ResponseEntity.ok(activityResponse);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<ActitivitiesListResponse> getAllActivities(@PathVariable UUID id) {
        List<DailyActivitiesDTO> activityDetails = this.activityService.getAllActivitiesGroupedByDate(id);
        ActitivitiesListResponse actitivitiesResponse = new ActitivitiesListResponse(activityDetails);
        return ResponseEntity.ok(actitivitiesResponse);
    }

    @DeleteMapping("/{id}/activities")
    public ResponseEntity<String> deleteActivity(@PathVariable UUID id) {
        String activity = activityService.deleteActivitie(id);
        return ResponseEntity.ok(activity);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponseDTO> createLink(@PathVariable UUID id, @RequestBody @Valid LinkRequestDTO data) {
        Trip trip = this.tripService.getTrip(id);
        LinkResponseDTO linkResponseDTO = this.linkService.registerLink(trip, data);
        return ResponseEntity.ok(linkResponseDTO);
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkDetails>> getAllLinks(@PathVariable UUID id) {
        List<LinkDetails> linkDetails = this.linkService.getAllLinkFromId(id);
        return ResponseEntity.ok(linkDetails);
    }

    @DeleteMapping("/{id}/links")
    public ResponseEntity<String> deleteLink(@PathVariable UUID id) {
        String link = linkService.deleteLink(id);
        return ResponseEntity.ok(link);
    }
}
