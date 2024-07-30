package br.com.planner.planner.domain.verifyparticipant;

import br.com.planner.planner.domain.participant.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Table(name = "verify_participant")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    @Column(nullable = false)
    private UUID uuid;
    @Column(nullable = false)
    private Instant timer;
    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false, unique = true)
    private Participant participant;
}
