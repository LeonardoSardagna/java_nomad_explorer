package br.com.planner.planner.domain.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDetails(UUID id, String title, LocalDateTime occurs_at) {
}
