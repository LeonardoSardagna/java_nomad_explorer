package br.com.planner.planner.domain.participant;

import java.util.UUID;

public record ParticipantDetails(UUID id, String name, String email, Boolean isConfirmed) {
}
