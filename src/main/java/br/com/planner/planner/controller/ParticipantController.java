package br.com.planner.planner.controller;

import br.com.planner.planner.domain.participant.Participant;
import br.com.planner.planner.domain.participant.ParticipantRequestDTO;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.repository.ParticipantRepository;
import br.com.planner.planner.service.DateFormaterService;
import br.com.planner.planner.service.ParticipantService;
import br.com.planner.planner.validation.ValidationData;
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

    @Autowired
    private DateFormaterService dateFormaterService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> DeleteParticipant(@PathVariable UUID id) {
        Participant participant = participantService.getParticipant(id);
        Optional<Trip> trip = participantRepository.BuscaDadosDaViagemDeAcordoComParticipnt(id);
        String startDate = dateFormaterService.dateFormater(trip.get().getStartsAt());
        String andDate = dateFormaterService.dateFormater(trip.get().getEndsAt());
        participantService.confirmationEmailParticipants(participant.getEmail(),
                "Nomad Explorer - Atualização sobre a Viagem",
                "Olá,\n" +
                "\n" +
                "Espero que você esteja bem!\n" +
                "\n" +
                "Gostaríamos de informá-lo(a) que, infelizmente, você foi retirado(a) da viagem organizada por "+trip.get().getOwnerName()+" através do Nomad Explorer. Seguem os detalhes da viagem:\n" +
                "\n" +
                "Destino: " + trip.get().getDestination() + "\n" +
                "Data de Início: " + startDate + "\n" +
                "Data de Término: " + andDate + "\n" +
                "\n" +
                "Entendemos que isso pode ser uma notícia decepcionante, e pedimos desculpas por qualquer inconveniente causado. Se houver alguma dúvida ou se precisar de mais informações, por favor, não hesite em entrar em contato conosco.\n" +
                "\n" +
                "Agradecemos sua compreensão." + "\n" +
                "\n" +
                "Atenciosamente,\n" +
                "\n" +
                "Nomad Explorer\n" +
                "nomadexplorer@gmail.com");
        participantRepository.deleteById(id);
        return ResponseEntity.ok("Participante deletado com sucesso!");
    }

    @GetMapping("/verify/{uuid}")
    public ResponseEntity<String> verifyParticipant(@PathVariable String uuid) {
        String result = participantService.verifyRegister(uuid);
        return ResponseEntity.ok(result);
    }
}
