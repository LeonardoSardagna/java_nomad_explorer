package br.com.planner.planner.service;

import br.com.planner.planner.domain.participant.Participant;
import br.com.planner.planner.domain.participant.ParticipantCreateResponse;
import br.com.planner.planner.domain.participant.ParticipantDetails;
import br.com.planner.planner.domain.trip.Trip;
import br.com.planner.planner.domain.verifyparticipant.VerifyParticipant;
import br.com.planner.planner.repository.ParticipantRepository;
import br.com.planner.planner.repository.VerifyParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
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

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite
                .stream()
                .map(email -> new Participant(email, trip))
                .toList();
        this.participantRepository.saveAll(participants);

        for (Participant participant : participants) {
            VerifyParticipant verifyParticipant = new VerifyParticipant();
            verifyParticipant.setParticipant(participant);
            verifyParticipant.setUuid(UUID.randomUUID());
            verifyParticipant.setTimer(Instant.now().plusMillis(180000));
            verifyParticipantRepository.save(verifyParticipant);
        }
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        Participant participant = new Participant(email, trip);
        this.participantRepository.save(participant);
        return new ParticipantCreateResponse(participant.getId());
    }

    public List<ParticipantDetails> getAllParticipantsFromEvents(UUID id) {
        return this.participantRepository.findByTripId(id)
                .stream().map(trip -> new ParticipantDetails(
                        trip.getId(),
                        trip.getName(),
                        trip.getEmail(),
                        trip.getIsConfirmed())).toList();
    }

    public String confirmationEmailParticipants(String emails_to_invite, String subject, String message) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(this.ownerEmail);
            simpleMailMessage.setTo(emails_to_invite);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            javaMailSender.send(simpleMailMessage);
            return "Email enviado com sucesso" + emails_to_invite;
        } catch (Exception e) {
            return "email não enviado" + e.getLocalizedMessage();
        }
    }

    public String verifyRegister(String uuid) {
        Optional<VerifyParticipant> verifyParticipant = verifyParticipantRepository.findByParticipantId(UUID.fromString(uuid));
        if (verifyParticipant.isPresent()) {
            VerifyParticipant participant = verifyParticipant.get();
            if (participant.getTimer().compareTo(Instant.now()) < 0) {
                verifyParticipantRepository.delete(participant);
                participantRepository.deleteById(participant.getParticipant().getId());
                return "Tempo de verificação expirado";
            }
        } else {
            return "Não possui cadastrado";
        }
        return "Usuario esta no tempo limite para confirmação";
    }
}
