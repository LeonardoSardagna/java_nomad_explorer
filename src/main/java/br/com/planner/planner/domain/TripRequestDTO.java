package br.com.planner.planner.domain;

import java.util.List;

public record TripRequestDTO(
        String ownerName,
        String ownerEmail,
        String destination,
        List<String> emails_to_invite,
        String starts_at,
        String ends_at) {
}
