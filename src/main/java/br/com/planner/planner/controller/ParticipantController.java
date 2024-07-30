package br.com.planner.planner.controller;

import br.com.planner.planner.domain.participant.Participant;
import br.com.planner.planner.domain.participant.ParticipantRequestDTO;
import br.com.planner.planner.domain.verifyparticipant.VerifyParticipant;
import br.com.planner.planner.repository.ParticipantRepository;
import br.com.planner.planner.repository.VerifyParticipantRepository;
import br.com.planner.planner.service.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantService participantService;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody @Valid ParticipantRequestDTO data) {

        Optional<Participant> participant = participantRepository.findById(id);

        if (participant.isPresent()) {
            Participant rawParticipant = participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(data.name());

            this.participantRepository.save(rawParticipant);
            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/verify/{uuid}")
    public String verifyParticipant (@PathVariable String uuid){
        return participantService.verifyRegister(uuid);
    }
}
