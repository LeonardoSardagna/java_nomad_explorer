package br.com.planner.planner.Controller;

import br.com.planner.planner.Repository.TripRepository;
import br.com.planner.planner.domain.Trip;
import br.com.planner.planner.domain.TripRequestDTO;
import br.com.planner.planner.domain.TripResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @PostMapping
    public ResponseEntity<TripResponseDTO> createTrip(@RequestBody TripRequestDTO data ){
        Trip newTrip = new Trip(data);

        tripRepository.save(newTrip);

        return ResponseEntity.ok(new TripResponseDTO(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> tripDetails(@PathVariable UUID id){
        Optional<Trip> planner = tripRepository.findById(id);
        return planner.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
}
