package br.com.planner.planner.domain.participant;

import java.util.UUID;

public record ParticipantDetails(Long id, String name, String email, Boolean isConfirmed) {
}
