package br.com.planner.planner.domain.trip;

import java.time.LocalDateTime;
import java.util.UUID;

public record TripDetails(
        UUID id,
        String ownerName,
        String ownerEmail,
        String destination,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Boolean isConfirmed
) {
}
