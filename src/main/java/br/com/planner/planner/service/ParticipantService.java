package br.com.planner.planner.service;

import br.com.planner.planner.domain.participant.Participant;
import br.com.planner.planner.domain.participant.ParticipantCreateResponse;
import br.com.planner.planner.domain.participant.ParticipantDetails;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.verifyparticipant.VerifyParticipant;
import br.com.planner.planner.infra.exception.ValidationException;
import br.com.planner.planner.infra.exception.ValidationNotFoundException;
import br.com.planner.planner.repository.ParticipantRepository;
import br.com.planner.planner.repository.TripRepository;
import br.com.planner.planner.repository.VerifyParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private VerifyParticipantRepository verifyParticipantRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String ownerEmail;

    @Autowired
    private TripRepository tripRepository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite
                .stream()
                .map(email -> new Participant(email, trip))
                .toList();
        this.participantRepository.saveAll(participants);

        for (Participant participant : participants) {
            VerifyParticipant verifyParticipant = new VerifyParticipant();
            verifyParticipant.setParticipant(participant);
            verifyParticipant.setTimer(Instant.now().plusMillis(900000));
            verifyParticipantRepository.save(verifyParticipant);
        }
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        Participant participant = new Participant(email, trip);
        this.participantRepository.save(participant);
        this.confirmationEmailParticipants(
                participant.getEmail(),
                "Nomad Explorer - Convite para a Próxima Viagem!",
                "Olá,\n" +
                        "\n" +
                        "Espero que você esteja bem!\n" +
                        "\n" +
                        trip.getOwnerName() + " gostaria de convidá-lo(a) para uma viagem que está organizando através do Nomad Explorer. Seguem os detalhes:\n" +
                        "\n" +
                        "Destino: " + trip.getDestination() + "\n" +
                        "Data de Início: " + trip.getStartsAt() + "\n" +
                        "Data de Término: " + trip.getEndsAt() + "\n" +
                        "\n" +
                        "Para confirmar sua presença e obter mais detalhes, por favor, clique no link de confirmação abaixo e insira o seu nome:\n" +
                        "\n" +
                        "http://localhost:5173/trips/" + trip.getId() + "/confirm/" + participant.getId() + "\n" +
                        "\n" +
                        "Estou ansioso(a) para ter você conosco nesta viagem incrível! Se tiver alguma dúvida ou precisar de mais informações, não hesite em entrar em contato.\n" +
                        "\n" +
                        "Nomad Explorer\n" +
                        "nomadexplorer@gmail.com");
        return new ParticipantCreateResponse(participant.getId());
    }

    public List<ParticipantDetails> getAllParticipantsFromEvents(UUID id) {

        Optional<Trip> rawTrip = tripRepository.findById(id);

        if (rawTrip.isPresent()) {
            return this.participantRepository.findByTripId(id)
                    .stream().map(trip -> new ParticipantDetails(
                            trip.getId(),
                            trip.getName(),
                            trip.getEmail(),
                            trip.getIsConfirmed())).toList();
        } else {
            throw new ValidationNotFoundException("Viagem não encontrada");
        }
    }

    public void confirmationEmailParticipants(String emails_to_invite, String subject, String message) {

        try {
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//            simpleMailMessage.setFrom(this.ownerEmail);
//            simpleMailMessage.setTo(emails_to_invite);
//            simpleMailMessage.setSubject(subject);
//            simpleMailMessage.setText(message);
//            javaMailSender.send(simpleMailMessage);
            System.out.println("Email enviado");
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    @Transactional
    public String verifyRegister(String uuid) {
        Optional<VerifyParticipant> optionalVerifyParticipant = verifyParticipantRepository.findByParticipantId(UUID.fromString(uuid));

        if (optionalVerifyParticipant.isEmpty()) {
            throw new ValidationException("O usuário não possui cadastro");
        }

        VerifyParticipant verifyParticipant = optionalVerifyParticipant.get();
        Optional<Participant> optionalParticipant = participantRepository.findById(verifyParticipant.getParticipant().getId());

        if (optionalParticipant.isEmpty()) {
            throw new ValidationException("Participante não encontrado");
        }

        Participant participant = optionalParticipant.get();

        if (verifyParticipant.getTimer().compareTo(Instant.now()) <= 0 && !participant.getIsConfirmed()) {
            participantRepository.deleteById(participant.getId());
            verifyParticipantRepository.deleteById(verifyParticipant.getId());
            return "Tempo de verificação expirado";
        }
        return "Usuário está no tempo limite para confirmação";
    }
}
